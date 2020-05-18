#include <Smartcar.h>
#include <Wire.h>
#include <WiFi.h>
#include <WebServer.h>
#include <ESPmDNS.h>

const auto pulsesPerMeter = 600;
const int START_SPEED = 40;
const int MIN_OBSTACLE_DISTANCE = 30;
const int BAUD_RATE = 115200;
const int STOP = 0;
const char* ssid = "SSID";
const char* password = "password";

int frontSensorReading;
int carSpeed;

//Ultrasonic sensor setup
const int TRIGGER_PIN = 5; //D5
const int ECHO_PIN = 18; //D18
const unsigned int MAX_DISTANCE = 150;
SR04 front(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

WebServer server(80);

BrushedMotor leftMotor(smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);

GY50 gyroscope(14);

DirectionlessOdometer leftOdometer(
    smartcarlib::pins::v2::leftOdometerPin, []() { leftOdometer.update(); }, pulsesPerMeter);
DirectionlessOdometer rightOdometer(
    smartcarlib::pins::v2::rightOdometerPin, []() { rightOdometer.update(); }, pulsesPerMeter);

SmartCar car(control, gyroscope, leftOdometer, rightOdometer);

 //Static Local IP configuration for identifying car. Gateway and subnet needs to be adjusted according to local network settings.
//IPAddress local_IP(192, 168, 1, 200);
//IPAddress gateway(192, 168, 1, 1);
//IPAddress subnet(255, 255, 255, 0);

void setup(){
  Serial.begin(BAUD_RATE);
  delay(10);
  Wire.begin();
  connectToWiFi();
  carSpeed = START_SPEED;

  // Car URL to handle the request
  server.on("/request", HTTP_GET, handleRequest);
  server.on("/status", HTTP_GET, handleStatus);
  server.onNotFound(handleNotFound);

  server.begin();
}
void loop() {
  server.handleClient();

  frontSensorReading = front.getDistance();
  // OBSTACLE AVOIDANCE
  // Stop when distance from the front sensor is less than MIN_OBSTACLE_DISTANCE and
  // the car is moving forward,
  // disregard 0 reading because its a null reading from the sensor
  if (frontSensorReading <= MIN_OBSTACLE_DISTANCE && frontSensorReading > 0 && carSpeed > 0){
    car.setSpeed(STOP);
  }else if (WiFi.status() != WL_CONNECTED){
    car.setSpeed(STOP);
    connectToWiFi();
  }
}

void connectToWiFi(){

//Used when static IP is available
//  if (!WiFi.config(local_IP, gateway, subnet)) {
//    Serial.println("STA Failed to configure");
//  }

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  //wait for wifi to connect
  while (WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("Connected to the WiFi network");
  Serial.println(WiFi.localIP());
}

void setCarMovement(int newSpeed, int newAngle) {
  setCarAngle(newAngle);
  setCarSpeed(newSpeed);
}
void setCarAngle(int newAngle){
  car.setAngle(newAngle);
}
void stopCar() {
  carSpeed = STOP;
  car.setSpeed(carSpeed);
}
void setCarSpeed(int newSpeed){
  if(newSpeed == 0){
    stopCar();
  }else if(newSpeed < carSpeed){
    while(newSpeed < carSpeed){
      carSpeed--;
      car.setSpeed(carSpeed);
    }
  }else if(newSpeed > carSpeed){
    while(newSpeed > carSpeed){
      carSpeed++;
      car.setSpeed(carSpeed);
    }
  }else{
    car.setSpeed(carSpeed);
  }
}
// Turns the car, on the spot, the specified degrees and then resets the car's angle
// Adapted version from SmartCar Shield Library's automated movements example, as seen
// in the link: https://platisd.github.io/smartcar_shield/automated_movements_8ino-example.html
void turnCar(int turningAngle) {
    turningAngle %= 360; // Put degrees in a (-360,360) scale
    if (turningAngle == 0)
    {
        return;
    }
    if (turningAngle > 0)
    {
        setCarAngle(90);
    }
    else
    {
        setCarAngle(-90);
    }
    const auto initialHeading    = car.getHeading();
    bool hasReachedTargetDegrees = false;
    while (!hasReachedTargetDegrees)
    {
        car.update();
        auto currentHeading = car.getHeading();
        if (turningAngle < 0 && currentHeading > initialHeading)
        {
            // If we are turning left and the current heading is larger than the
            // initial one (e.g. started at 10 degrees and now we are at 350), we need to subtract
            // 360 so to eventually get a signed displacement from the initial heading (-20)
            currentHeading -= 360;
        }
        else if (turningAngle > 0 && currentHeading < initialHeading)
        {
            // If we are turning right and the heading is smaller than the
            // initial one (e.g. started at 350 degrees and now we are at 20), so to get a signed
            // displacement (+30)
            currentHeading += 360;
        }
        // Degrees turned so far is initial heading minus current (initial heading
        // is at least 0 and at most 360. To handle the "edge" cases we subtracted or added 360 to
        // currentHeading)
        int degreesTurnedSoFar  = initialHeading - currentHeading;
        hasReachedTargetDegrees = smartcarlib::utils::getAbsolute(degreesTurnedSoFar)
                                  >= smartcarlib::utils::getAbsolute(degrees);
    }
    setCarAngle(0);
}
void handleRequest() {
  if (!server.hasArg("type") && !server.hasArg("speed") && !server.hasArg("angle") && !server.hasArg("control")) {
    server.send(404);
    return;
  }
  String appRequest = server.arg("type");
  int speedRequest = (server.arg("speed")).toInt();
  int angleRequest = (server.arg("angle")).toInt();
  String controlRequest = server.arg("control");
  if (appRequest.equals("move") && controlRequest.equals("manual")) {
    setCarMovement(speedRequest, angleRequest);
    server.send(200);
  }
  else if (appRequest.equals("move") && controlRequest.equals("voice")) {
      turnCar(angleRequest);
      setCarSpeed(speedRequest);
      server.send(200);
  }
  else if (appRequest.equals("stop")) {
    stopCar();
    server.send(200);
  }else {
    server.send(404);
  }
}
void handleStatus(){
  server.send(200, "text/plain", "status");
}
void handleNotFound(){
  server.send(404);
}