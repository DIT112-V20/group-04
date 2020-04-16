#include <Smartcar.h>
#include <Wire.h>
#include <VL53L0X.h>
#include <WiFi.h>
#include <WebServer.h>

const int START_SPEED = 50;
const int MIN_OBSTACLE_DISTANCE = 300;
const int BAUD_RATE = 115200;
const int STOP = 0;
const char* ssid = "SSID";
const char* password = "PASSWORD";

int frontSensorReading;

WebServer server(80);

BrushedMotor leftMotor(smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);
SimpleCar car(control);
VL53L0X sensor;

// Static Local IP configuration for identifying car.
IPAddress local_IP(192, 168, 1, 200);
IPAddress gateway(192, 168, 1, 1);
IPAddress subnet(255, 255, 255, 0);

void setup(){
  Serial.begin(BAUD_RATE);
  delay(10);
  Wire.begin();
  connectToWiFi();

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

void moveForward() {
  car.setSpeed(START_SPEED);
}

void stopCar() {
  car.setSpeed(STOP);
}
void handleRequest() {
  if (!server.hasArg("type")) {
    server.send(404);
    return;
  }
  String appRequest = server.arg("type");
  if (appRequest.equals("forward")) {
    moveForward();
    server.send(200);
  }
  else if (appRequest.equals("stop")) {
    stopCar();
    server.send(200);
  }
  else  if (appRequest.equals("status")) {
    server.send(200);
  }
  else {
    server.send(404);
  }
}