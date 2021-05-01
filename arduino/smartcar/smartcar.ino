#include <Smartcar.h>
#include <vector>
#include <MQTT.h>
#include <WiFi.h>
#ifdef __SMCE__
#include <OV767X.h>
#endif

#ifndef __SMCE__
WiFiClient net;
#endif
MQTTClient mqtt;


const int fSpeed   = 25;  // 25% of the full speed forward
const int bSpeed   = -10; // 10% of the full speed backward
const int changeSpeed = 10;
const int maxSpeed = 100;
const int minSpeed = 10;

const int triggerDist = 0;
const int lDegrees = -90; // degrees to turn left
const int rDegrees = 90; // degrees to turn right

int currentSpeed = 0;

const auto triggerPin = 6;
const auto echoPin = 7;
const auto maxDistance = 400;
const auto oneSecond = 1000UL;

const auto pulsePerMeter = 600;


ArduinoRuntime arduinoRuntime;

//Motors
DirectionalOdometer leftOdometer{
    arduinoRuntime,
    smartcarlib::pins::v2::leftOdometerPins,
    []() { leftOdometer.update(); },
pulsePerMeter};
DirectionalOdometer rightOdometer{
    arduinoRuntime,
    smartcarlib::pins::v2::rightOdometerPins,
    []() { rightOdometer.update(); },
    pulsePerMeter}; // odometer constructor
	
BrushedMotor leftMotor{arduinoRuntime, smartcarlib::pins::v2::leftMotorPins};
BrushedMotor rightMotor{arduinoRuntime, smartcarlib::pins::v2::rightMotorPins};

//Steering 
DifferentialControl control{leftMotor, rightMotor};

// Ultrasonic sensor 
SR04 front(arduinoRuntime, triggerPin, echoPin, maxDistance);
SR04 sensor(arduinoRuntime, triggerPin, echoPin);

SimpleCar car{control};


// different options used when serial input is being executed
std::vector<char> frameBuffer;
      
 void stopVehicle(){
  car.setSpeed(0);
  currentSpeed = 0; 
 }

 void goForward(int speed){
    car.setSpeed(speed);
  currentSpeed = speed; 
 }

 void goBackward(int speed){
    car.setSpeed(speed);
  currentSpeed = speed;
 }
 
 void turnLeft(){
    car.setAngle(lDegrees);
    delay(2000);
    car.setAngle(0);
 }

 void turnRight()
 {
 car.setAngle(rDegrees);
  delay(2000);
  car.setAngle(0);
 }
 
 void turnLeftWhenStoped()  // when car doesn't move it will turn on spot to left
 {
    car.setSpeed(fSpeed);
    currentSpeed = fSpeed;
    car.setAngle(lDegrees);
		delay(6000);
		car.setAngle(0);
    stopVehicle();
 }

  void turnRightWhenStoped() // when car doesn't move it will turn on spot to right
 {
    car.setSpeed(fSpeed);
    currentSpeed = fSpeed;
    car.setAngle(rDegrees);
		delay(6000);
		car.setAngle(0);
    stopVehicle();
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
 #ifdef __SMCE__
  mqtt.begin("aerostun.dev", 1883, WiFi);
 #else
  mqtt.begin(net);
  #endif
  if (mqtt.connect("arduino", "public", "public")) {
    mqtt.subscribe("/smartcar/group16/control/#", 1);
    mqtt.onMessage([](String topic, String message) {
      if (topic == "/smartcar/group16/control/throttle") {
        car.setSpeed(message.toInt());
      } else if (topic == "/smartcar/group16/control/steering") {
        car.setAngle(message.toInt());
      } else {
        Serial.println(topic + " " + message);
      }
    });
  }
}

void loop() {
   // handleInput();
    obstacleAvoidance();
  {
    Serial.println((leftOdometer.getDistance() + rightOdometer.getDistance())/2);
}
    
    if (mqtt.connected()) {
    mqtt.loop();
     const auto currentTime = millis();
#ifdef __SMCE__
#endif
    static auto previousTransmission = 0UL;
    if (currentTime - previousTransmission >= oneSecond) {
      previousTransmission = currentTime;
      const auto distance = String(front.getDistance());
      mqtt.publish("/smartcar/group16/ultrasound/front", distance);
    }
  }
#ifdef __SMCE__
  // Avoid over-using the CPU if we are running in the emulator
  delay(35);
#endif
  }

//obstacle avoidance 
void obstacleAvoidance(){
    unsigned int distance = front.getDistance();
    unsigned int triggerDist = 100;
  if (distance > 0 && distance <= triggerDist){ //third condition added that checks if the car is moving forward.
      car.setSpeed(0);
      delay(50);
      car.setSpeed(-70);
      delay(50);
      car.setSpeed(0);
  }
} 

/*void handleInput(){ // handle serial input if there is any
    if (Serial.available()){
        char input = Serial.read(); // read everything that has been received so far and log down
                                    // the last entry
        switch (input) {
        case 'f': // go ahead in medium speed 
      if (currentSpeed>0){
        goForward(currentSpeed); // starts on 50 %, contiunes based on the speed before it stopped.
      }else{ // 
        goForward(fSpeed);
      }
            break;
        case 'b': // go back 
      if (currentSpeed<0){
        goBackward(currentSpeed); // starts on 50 %, contiunes based on the speed before it stopped.
      }else{
        goBackward(bSpeed);
      }
            break;
        case 's': // stop 
            stopVehicle();
            break;
        case 'l': // turn left
             if(currentSpeed>0)
            {
              turnLeft();
            }
            else
            {
              turnLeftWhenStoped();
            }
            break;
        case 'r': // turn right
             if(currentSpeed>0)
            {
              turnRight();
            }
            else
            {
              turnRightWhenStoped();
            }
            break;
        case 'd': // the car decelerates
            decelerate(currentSpeed);
            break;
        case 'a': // the car accelerate  
            accelerate(currentSpeed);
            break;
        default: // if you receive something that           you don't know, just stop
            stopVehicle();
        } 
    }
}*/
