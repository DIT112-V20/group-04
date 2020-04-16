#include <Smartcar.h>
#include <Wire.h>
#include <VL53L0X.h>

const int START_SPEED = 50;
const int MIN_OBSTACLE_DISTANCE = 300;
const int BAUD_RATE = 9600;
const int STOP = 0;

BrushedMotor leftMotor(smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);
VL53L0X sensor;
int frontSensorReading;

void setup()
{
  Serial.begin(BAUD_RATE);
  Wire.begin();

  // Allow half a second to connect the sensor
  sensor.setTimeout(500);
  if (!sensor.init())
  {
    Serial.println("Failed to detect ,

  // Start continuous back-to-back mode 
  // (take readings as fast as possible).
  sensor.startContinuous();

  car.setSpeed(START_SPEED);
}

void loop() {
  frontSensorReading = sensor.readRangeContinuousMillimeters();
  
  // Stop when distance is less than MIN_OBSTACLE_DISTANCE and
  // disregard 0 reading because its a null reading from the sensor
  if (frontSensorReading <= MIN_OBSTACLE_DISTANCE && frontSensorReading > 0){
    car.setSpeed(STOP);
  }
  
}
