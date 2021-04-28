#include <Smartcar.h>
#include <vector>
#include <MQTT.h>
#include <WiFi.h>

#ifdef __SMCE__
#include <OV767X.h>
#endif

#ifndef  __SMCE__
WiFiClient net;
#endif
MQTTClient mqtt;



const int fSpeed   = 25;  // 25% of the full speed forward
const int bSpeed   = -10; // 10% of the full speed backward
const int changeSpeed = 10;
const int maxSpeed = 100;
const int minSpeed = 10;
const int triggerDist = 100;
const int lDegrees = -80; // degrees to turn left
const int rDegrees = 80; // degrees to turn right
const int SERVO_PIN = 8;
const int ESC_PIN = 9;
const int BACK_IR_PIN =3;
const int FRONT_IR_PIN =3;


 
//ServoMotor steering (SERVO_PIN);
//ServoMotor throttling (ESC_PIN);
//AckermanControl control(steering, throttling);
 
int currentSpeed = fSpeed;

unsigned short TRIGGER_PIN = 6;
unsigned short ECHO_PIN = 7;
const unsigned int MAX_DISTANCE = 1000;

ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);
SimpleCar car(control);
SR04 sensor(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE); 
GP2D120 backIRSensor(arduinoRuntime,BACK_IR_PIN);  //measure in short distances
GP2D120 frontIRSensor(arduinoRuntime,FRONT_IR_PIN);



void setup() {
  Serial.begin(9600);
#ifndef __SMCE__ 
  mqtt.begin("aerostun.dev", 1883, WiFi);  // Will connect to TA server
  //mqtt.begin(WiFi); // to localhost
#else
  mqtt.begin(net);
#endif
  if (mqtt.connect("arduino", "public", "public")) {
    mqtt.subscribe("/smartcar/control/#", 1);
    mqtt.onMessage([](String topic, String message) {
      if (topic == "/smartcar/control/throttle") {
        car.setSpeed(message.toInt());
      } else if (topic == "/smartcar/control/steering") {
        car.setAngle(message.toInt());
      } else {
        Serial.println(topic + " " + message);
      }
    });
  }
}


 void loop() {
   if (mqtt.connected()) {
     mqtt.loop();}
    
    handleInput();
   obstacleAvoidance();
   ObstacleAvoidanceBack();

   }


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
 
 void turnLeft(){
    car.setAngle(lDegrees);
    delay(2000);
    car.setAngle(0);
 }

 void turnRight(){
 car.setAngle(rDegrees);
  delay(2000);
  car.setAngle(0);
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

//void setup() {
  //Serial.begin(9600);
//}

/* void loop() {
   handleInput();
  // setSpeedAndAngle();
   obstacleAvoidance();
   ObstacleAvoidanceBack();
} */

void handleInput(){ // handle serial input if there is any
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
            turnLeft();
            break;
        case 'r': // turn right
            turnRight();
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
}

void obstacleAvoidance(){
   int distance = sensor.getDistance();
  if (distance > 0 && distance < triggerDist && currentSpeed >= 0 ){ //third condition added that checks if the car is moving forward.
      car.setSpeed(0);
  }

}
void ObstacleAvoidanceBack(){
    int backDistance=backIRSensor.getDistance();
    if(backDistance >0 && backDistance <triggerDist){
      car.setSpeed(0);
    }

}

void setSpeedAndAngle()
{
    // handle serial input if there is any
    if (Serial.available())
    {
        String input = Serial.readStringUntil('\n');
        if (input.startsWith("m"))
        {  int throttle = input.substring(1).toInt();
            car.setSpeed(throttle);
        }
        else if (input.startsWith("t"))
        {
            int deg = input.substring(1).toInt();
            car.setAngle(deg);
        }
    }
}