#include <Smartcar.h>
#include <Wire.h>
#include <VL53L0X.h>
#include <WiFi.h>
#include <WebServer.h>

const auto pulsesPerMeter = 600;
const int START_SPEED = 40;
const int MIN_OBSTACLE_DISTANCE = 300;
const int BAUD_RATE = 115200;
const int STOP = 0;
const int SENSOR_DELAY = 10;
const int NR_OF_READINGS = 5;
const char* ssid = "ssid";
const char* password = "password";

int frontSensorReading;
int carSpeed;

WebServer server(80);

BrushedMotor leftMotor(smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);

GY50 gyroscope(14);
VL53L0X sensor;

DirectionlessOdometer leftOdometer(
    smartcarlib::pins::v2::leftOdometerPin, []() { leftOdometer.update(); }, pulsesPerMeter);
DirectionlessOdometer rightOdometer(
    smartcarlib::pins::v2::rightOdometerPin, []() { rightOdometer.update(); }, pulsesPerMeter);

SmartCar car(control, gyroscope, leftOdometer, rightOdometer);

// Static Local IP configuration for identifying car.
IPAddress local_IP(192, 168, 1, 200);
IPAddress gateway(192, 168, 1, 1);
IPAddress subnet(255, 255, 255, 0);

void setup(){
  Serial.begin(BAUD_RATE);
  delay(10);
  Wire.begin();
  //car.enableCruiseControl();
  connectToWiFi();
  carSpeed = START_SPEED;

  // Allow half a second to connect the sensor
  sensor.setTimeout(500);
  if (!sensor.init()){
    Serial.println("Failed to detect and initialize sensor!");
    while (1) {}
  }

  // Start continuous back-to-back mode
  // (take readings as fast as possible).
  sensor.startContinuous();

  // Car URL to handle the request
  server.on("/request", HTTP_GET, handleRequest);
  server.on("/status", HTTP_GET, handleStatus);
  server.onNotFound(handleNotFound);

  server.begin();
}
void loop() {
  server.handleClient();

  frontSensorReading = getMedianSensorReading(NR_OF_READINGS);
//   Stop when distance is less than MIN_OBSTACLE_DISTANCE and
//   disregard 0 reading because its a null reading from the sensor
  if (obstacleDetectedFront()){
    stopCar();
  } else if (WiFi.status() != WL_CONNECTED){
    stopCar();
    connectToWiFi();
  }
}

void connectToWiFi(){
  if (!WiFi.config(local_IP, gateway, subnet)) {
    Serial.println("STA Failed to configure");
  }
  WiFi.begin(ssid, password);
  // Wait for wifi to connect
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

// Sets the cars speed while also doing basic obstacle avoidance
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
void handleRequest() {
  if (!server.hasArg("type") && !server.hasArg("speed") && !server.hasArg("angle")) {
    server.send(404);
    return;
  }
  String appRequest = server.arg("type");
  int speedRequest = (server.arg("speed")).toInt();
  int angleRequest = (server.arg("angle")).toInt();
  if (appRequest.equals("move")) {
    setCarMovement(speedRequest, angleRequest);
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

// Returns the median of the given amount of sensor readings, in mm,
// where iterations >0
int getMedianSensorReading(int iterations) {
    // Return a negative value to indicate error
    if (iterations < 1) {
     return -1;
    }

    //Initialize variables
    int readings[iterations];
    int i = 0;
    unsigned long previousTime = millis();
    unsigned long currentTime;

    //Iterate until the given amount of readings have been taken
    while (i < iterations) {
        currentTime = millis();
        if (currentTime - previousTime >= SENSOR_DELAY) { // Only take reading when SENSOR_DELAY ms have passed
            readings[i] = getSensorReading();
            // Serial.print("Sensor reading: "); // For debugging
            // Serial.println(readings[i]); // For debugging
            i++;
         }
    }
    //Calculate the median of the readings taken
    int median = smartcarlib::utils::getMedian(readings, iterations);
    // Serial.print("Sensor median: "); // For debugging
    // Serial.println(median); // For debugging

    return median;
}

// Returns the current sensor reading, in mm
int getSensorReading() {
    return sensor.readRangeContinuousMillimeters();
}

// Returns true when an obstacle is within MIN_OBSTACLE_DISTANCE of the front sensor
bool obstacleDetectedFront() {
    if (frontSensorReading <= MIN_OBSTACLE_DISTANCE && frontSensorReading > 0) {
        // Serial.println("Obstacle detected!"); // For debugging
        return true;
    } else {
        return false;
    }
}