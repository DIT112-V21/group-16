#include <Smartcar.h>

const int fSpeed   = 50;  // 50% of the full speed forward
const int bSpeed   = -40; // 40% of the full speed backward
const int changeSpeed = 10;
const int maxSpeed = 100;
const int minSpeed = 10;
//const int cruiseSpeed = 40;
const int triggerDist = 200;
const int lDegrees = -75; // degrees to turn left

int currentSpeed = fSpeed;

unsigned short TRIGGER_PIN = 6;
unsigned short ECHO_PIN = 7;
const unsigned int MAX_DISTANCE = 1000;

ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor{arduinoRuntime, smartcarlib::pins::v2::leftMotorPins};
BrushedMotor rightMotor{arduinoRuntime, smartcarlib::pins::v2::rightMotorPins};
DifferentialControl control{leftMotor, rightMotor};
SimpleCar car{control};
SR04 sensor(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

 void stopVehicle(){
  car.setSpeed(0);
 }

 void goForward(int speed){
  	car.setSpeed(speed);
	currentSpeed = speed; 
 }

 void goBackward(int speed){
  	car.setSpeed(speed);
	currentSpeed = speed;
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

void setup() {
  Serial.begin(9600);
  //car.setSpeed(currentSpeed);
}
void loop() {
 
   handleInput();
   
  unsigned int distance = sensor.getDistance();
  if (distance > 0 && distance < triggerDist && currentSpeed >= 0 ){ //third condition added that checks if the car is moving forward.
      car.setSpeed(0);
    //car.setAngle(lDegrees);
    //car.setSpeed(cruiseSpeed);
  }
  //car.setAngle(0);
} 

void handleInput(){ // handle serial input if there is any
    if (Serial.available())
    {
        char input = Serial.read(); // read everything that has been received so far and log down
                                    // the last entry
        switch (input)
        {
        case 'f': // go ahead in medium speed 
            goForward(currentSpeed); // starts on 50 %, contiunes based on the speed before it stopped.
            break;
        case 'b': // go back 
            goBackward(bSpeed);
            break;
        case 's': // stop 
            stopVehicle();
            break;
        case 'd': // the car decelerates
            decelerate(currentSpeed);
            break;
        case 'a': // the car accelerate  s
            accelerate(currentSpeed);
            break;
        default: // if you receive something that you don't know, just stop
            stopVehicle();
        } 
    }
}