#include <Smartcar.h>
#include <Wire.h>
#include <VL53L0X.h>
#include <WiFi.h>
#include <WebSocketServer.h>

const auto pulsesPerMeter = 600;
const int START_SPEED = 40;
const int MIN_OBSTACLE_DISTANCE = 300;
const int BAUD_RATE = 115200;
const int STOP = 0;
const char* ssid = "ssid";
const char* password = "password";

int frontSensorReading;
int carSpeed;

WebSocketServer server(80);

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

  frontSensorReading = sensor.readRangeContinuousMillimeters();
  // Stop when distance is less than MIN_OBSTACLE_DISTANCE and
  // disregard 0 reading because its a null reading from the sensor
  if (frontSensorReading <= MIN_OBSTACLE_DISTANCE && frontSensorReading > 0){
    car.setSpeed(STOP);
  }else if (WiFi.status() != WL_CONNECTED){
    car.setSpeed(STOP);
    connectToWiFi();
  }
}

void connectToWiFi(){
  if (!WiFi.config(local_IP, gateway, subnet)) {
    Serial.println("STA Failed to configure");
  }
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
  server.send(200);
}
void handleNotFound(){
  server.send(404);
}