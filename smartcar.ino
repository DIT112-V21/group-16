#include <Smartcar.h>


const int cruiseSpeed = 40;
const int triggerDist = 200;

const int lDegrees = -75; // degrees to turn left

float distance;

unsigned short TRIGGER_PIN = 6;
unsigned short ECHO_PIN = 7;
const unsigned int MAX_DISTANCE = 1000;

ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor{arduinoRuntime, smartcarlib::pins::v2::leftMotorPins};
BrushedMotor rightMotor{arduinoRuntime, smartcarlib::pins::v2::rightMotorPins};
DifferentialControl control{leftMotor, rightMotor};
SimpleCar car{control};
SR04 sensor(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

void setup() {
  Serial.begin(9600);
  car.setSpeed(cruiseSpeed);
}
void loop() {
  distance = sensor.getDistance();
  if (distance > 0 && distance < triggerDist){
      car.setSpeed(0);
    //car.setAngle(lDegrees);
    //car.setSpeed(cruiseSpeed);
  }
  //car.setAngle(0);
} 
