#include <Smartcar.h>
#include <Wire.h>
#include <VL53L0X.h>
#include <WiFi.h>

const int START_SPEED = 50;
const int MIN_OBSTACLE_DISTANCE = 300;
const int BAUD_RATE = 115200;
const int STOP = 0;
const char* SSID = "ssid_goes_here";
const char* PASSWORD = "password_goes_here";
int frontSensorReading;

String clientRequest;
WiFiServer server(80);
WiFiClient client;
String result;

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

  clientRequest = "";
  Wire.begin();
  connectToWiFi();
  server.begin();

  // Allow half a second to connect the sensor
  sensor.setTimeout(500);
  if (!sensor.init()){
    Serial.println("Failed to detect and initialize sensor!");
    while (1) {}
  }
  // Start continuous back-to-back mode
  // (take readings as fast as possible).
  sensor.startContinuous();
}
void loop() {
  frontSensorReading = sensor.readRangeContinuousMillimeters();
  // Stop when distance is less than MIN_OBSTACLE_DISTANCE and
  // disregard 0 reading because its a null reading from the sensor
  if (frontSensorReading <= MIN_OBSTACLE_DISTANCE && frontSensorReading > 0){
    car.setSpeed(STOP);
  }
  // Stopping the car if the wi-fi connection is lost.
  if (WiFi.status() != WL_CONNECTED){
    car.setSpeed(STOP);
    connectToWiFi();
  }
    // Handle client requests
    client = server.available();
    if (!client) { return; }
    while(!client.available()){  delay(1); }
    clientRequest = (readIncomingRequest());
    // Removing the first five and last nine characters of the clients GET_REQUEST
    clientRequest.remove(0, 5);
    clientRequest.remove(clientRequest.length()-9,9);

    if (clientRequest == "stop") {
      handle_stop();
      delay(1);
    }

    if (clientRequest == "forward") {
      handle_forward();
      delay(1);
    }
    client.flush();


}
void connectToWiFi(){
  // Configures and checks if the configuration is successful.
  if (!WiFi.config(local_IP, gateway, subnet)) {
    Serial.println("STA Failed to configure");
  } else{
    WiFi.begin(SSID, PASSWORD);
  }
    // wait for wifi to connect
  while (WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("Connected to the WiFi network");
  Serial.println(WiFi.localIP());
}

String readIncomingRequest(){
  while(client.available()) {
    clientRequest = (client.readStringUntil('\r'));
    if ((clientRequest.indexOf("HTTP/1.1")>0)&&(clientRequest.indexOf("/favicon.ico")<0)){
    result = clientRequest;
    }
  }
  return result;
}

void handle_stop() {
  car.setSpeed(STOP);
}

void handle_forward() {
  car.setSpeed(START_SPEED);
}

