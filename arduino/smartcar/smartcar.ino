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

<<<<<<< Updated upstream
=======
 void stopVehicle(){
  car.setSpeed(0);
 }

 void goForward(int speed){
  car.setSpeed(speed);
 }

 void goBackward(int speed){
  car.setSpeed(speed);
 }
 
 void decelerate(int curSpeed){ // start at 50 
   int targetSpeed = curSpeed - changeSpeed; // decelerate by 10 
   if (currentSpeed > minSpeed){
    car.setSpeed(targetSpeed); // sets the speed to 40 
   currentSpeed = targetSpeed; // sets the current 40  
   }  
 }
 void accelerate(int curSpeed){
  int targetSpeed = curSpeed + changeSpeed;
   if (currentSpeed < maxSpeed){
  car.setSpeed(targetSpeed);
  currentSpeed = targetSpeed;
   }
 }

>>>>>>> Stashed changes
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
