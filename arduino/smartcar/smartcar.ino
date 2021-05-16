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
const int lDe = -90; // degrees to turn left on spot
const int rDe = 90; // degrees to turn right on spot
const auto oneSecond = 1000UL;
const auto pulsePerMeter = 600;
const int lDeg= -40;  //Degree for rotate on spot 
const int rDeg = 40;  //Degree for rotate on spot 
const int rotateSpeed = 70 ;  //speed for rotate on spot
const int bSpeed   = -40; // 40% of the full speed backward
int currentSpeed = 0;

ArduinoRuntime arduinoRuntime;

// Heading sensor 
GY50 gyro(arduinoRuntime, 37);

//Directional odometers
DirectionalOdometer leftOdometer{arduinoRuntime, smartcarlib::pins::v2::leftOdometerPins,[]() { leftOdometer.update(); },pulsePerMeter};
DirectionalOdometer rightOdometer{arduinoRuntime, smartcarlib::pins::v2::rightOdometerPins,[]() { rightOdometer.update(); }, pulsePerMeter}; // odometer constructor

//Infrared sensors
typedef GP2Y0A02 infrared;
infrared frontSensor(arduinoRuntime,0);
infrared rightSensor(arduinoRuntime,2);
infrared leftSensor(arduinoRuntime,1);
infrared backSensor(arduinoRuntime,3);

// Motors 
BrushedMotor leftMotor{arduinoRuntime, smartcarlib::pins::v2::leftMotorPins};
BrushedMotor rightMotor{arduinoRuntime, smartcarlib::pins::v2::rightMotorPins};

DifferentialControl control{leftMotor, rightMotor};

// Ultrasonic sensor 
SR04 front(arduinoRuntime, 6, 7, 400);
SR04 sensor(arduinoRuntime, 6, 7);

// SmartCar constructor 
SmartCar car(arduinoRuntime, control, gyro, leftOdometer, rightOdometer);

void rotateOnSpot(int targetDegrees, int speed)
{
    speed = smartcarlib::utils::getAbsolute(speed);
    targetDegrees %= 360; // put it on a (-360,360) scale
    if (!targetDegrees)
        return; // if the target degrees is 0, don't bother doing anything
    /* Let's set opposite speed on each side of the car, so it rotates on spot */
    if (targetDegrees > 0)
    { // positive value means we should rotate clockwise
        car.overrideMotorSpeed(speed,
                               -speed); // left motors spin forward, right motors spin backward
    }
    else
    { // rotate counter clockwise
        car.overrideMotorSpeed(-speed,
                               speed); // left motors spin backward, right motors spin forward
    }
    const auto initialHeading = car.getHeading(); // the initial heading we'll use as offset to
                                                  // calculate the absolute displacement
    int degreesTurnedSoFar
        = 0; // this variable will hold the absolute displacement from the beginning of the rotation
    while (abs(degreesTurnedSoFar) < abs(targetDegrees))
    { // while absolute displacement hasn't reached the (absolute) target, keep turning
        car.update(); // update to integrate the latest heading sensor readings
        auto currentHeading = car.getHeading(); // in the scale of 0 to 360
        if ((targetDegrees < 0) && (currentHeading > initialHeading))
        { // if we are turning left and the current heading is larger than the
            // initial one (e.g. started at 10 degrees and now we are at 350), we need to substract
            // 360, so to eventually get a signed
            currentHeading -= 360; // displacement from the initial heading (-20)
        }
        else if ((targetDegrees > 0) && (currentHeading < initialHeading))
        { // if we are turning right and the heading is smaller than the
            // initial one (e.g. started at 350 degrees and now we are at 20), so to get a signed
            // displacement (+30)
            currentHeading += 360;
        }
        degreesTurnedSoFar
            = initialHeading - currentHeading; // degrees turned so far is initial heading minus
                                               // current (initial heading
        // is at least 0 and at most 360. To handle the "edge" cases we substracted or added 360 to
        // currentHeading)
    }
    car.setSpeed(0); // we have reached the target, so stop the car
    // delay(100);
    // car.setSpeed(fSpeed);
    
}

std::vector<char> frameBuffer;

 void stopVehicle(){
  car.setSpeed(0);
  currentSpeed = 0; 
 }

 void turnLeftWhenStoped()  // when car doesn't move it will turn on spot to left
 {
    car.setSpeed(fSpeed);
    currentSpeed = fSpeed;
    car.setAngle(lDe);
    delay(6000);
    car.setAngle(0);
    stopVehicle();
 }

  void turnRightWhenStoped() // when car doesn't move it will turn on spot to right
 {
    car.setSpeed(fSpeed);
    currentSpeed = fSpeed;
    car.setAngle(rDe);
    delay(6000);
    car.setAngle(0);
    stopVehicle();
 }

  void autoTurnLeft()
 {
    car.setSpeed(fSpeed);
    currentSpeed = fSpeed;
    car.setAngle(lDe);
   delay(6000);
    car.setAngle(0);
 }

  void autoTurnRight()
 {
    car.setSpeed(fSpeed);
    currentSpeed = fSpeed;
    car.setAngle(rDe);
    delay(6000);
    car.setAngle(0);
 }

 
void setup() {
   Serial.begin(9600);
 #ifdef __SMCE__
 Camera.begin(QVGA, RGB888, 0); //qvga is a format 320 X 240, QVGA, RGB888, 0
  frameBuffer.resize(Camera.width() * Camera.height() * Camera.bytesPerPixel());
  mqtt.begin("aerostun.dev", 1883, WiFi);
 #else
  mqtt.begin(net);
  #endif
  if (mqtt.connect("arduino", "public", "public")) {
    mqtt.subscribe("/smartcar/group16/control/#", 1);
    mqtt.onMessage(+[](String& topic, String& message) {
      if (topic == "/smartcar/group16/control/throttle") {
        currentSpeed = message.toInt();
        car.setSpeed(currentSpeed);
      } else if (topic == "/smartcar/group16/control/steering") {
        car.setAngle(message.toInt());
      } else {
        Serial.println(topic + " " + message);
      }
    });
  }
}

void loop() {
    obstacleAvoidance();

    if (mqtt.connected()) {
    mqtt.loop();
    
     const auto currentTime = millis();
#ifdef __SMCE__
    static auto previousFrame = 0UL;
    if (currentTime - previousFrame >= 65) {
      previousFrame = currentTime;
      Camera.readFrame(frameBuffer.data());
      mqtt.publish("/smartcar/group16/camera", frameBuffer.data(), frameBuffer.size(),
                   false, 0);
    }
#endif
    static auto previousTransmission = 0UL;
    if (currentTime - previousTransmission >= oneSecond) 
    {
      previousTransmission = currentTime;
      const auto distance = String(front.getDistance());
      mqtt.publish("/smartcar/ultrasound/front", String(distance));
      mqtt.publish("/smartcar/group16/speed", String(car.getSpeed()));
      mqtt.publish("/smartcar/group16/distance", String(distanceInMeter()));
      mqtt.publish("/smartcar/group16/obstacleMsg", String(obstacleDetectionMessage()));
    }
  }
#ifdef __SMCE__
  // Avoid over-using the CPU if we are running in the emulator
  delay(35);
#endif
}  


// Changing odometer measurement from cm to km
int distanceInMeter(){
int distance = car.getDistance();
distance = distance/100;
return distance;
}

//obstacle avoidance message
int obstacleDetectionMessage(){
 unsigned int triggerDist = 200;
 unsigned int distance = front.getDistance();
  if (distance > 0 && distance < triggerDist){
    distance--;
    return distance;
     }
  }

//obstacle avoidance 
void obstacleAvoidance(){
    unsigned int distance = front.getDistance();
    unsigned int triggerDist = 100;
    const int sideTriggerDist = 30;
    unsigned int frontInfra = frontSensor.getDistance();
    unsigned int leftInfra = leftSensor.getDistance();
    unsigned int rightInfra = rightSensor.getDistance();
    unsigned int backInfra = backSensor.getDistance();

  if (distance > 0 && distance < triggerDist && currentSpeed >= 0 && leftInfra < sideTriggerDist && leftInfra > 0 ){ 
    //if there is an obstacle in front of the car and another one at the left side it will turn right.
     rotateOnSpot(rDeg , rotateSpeed);
     delay(300);
     car.setSpeed(fSpeed);
     if (distance > 0 && distance < 40 || (frontInfra<30 && frontInfra >0) )
     {
       autoTurnRight();// after turning from an obstacle if we faced another obstacle exremely close to prevent hitting that while turning it would
       //turn 90 degrees on spot to not hit it.
     }
     
     }
else if(distance > 0 && distance < triggerDist && currentSpeed >= 0 && rightInfra < sideTriggerDist && rightInfra > 0 ){
     //if there is an obstacle in front of the car and another one at the right side it will turn left.
     rotateOnSpot(lDeg , rotateSpeed);
          delay(300);
     car.setSpeed(fSpeed);
      if ((distance > 0 && distance < 40) || (frontInfra<30 && frontInfra >0) )
     {
       autoTurnLeft();
     }
     }
 else if (distance > 0 && distance < triggerDist && currentSpeed >= 0 ) { 
      //if there is an obstacle in front of the car it will turn right.
      rotateOnSpot(rDeg,rotateSpeed);
           delay(300);
     car.setSpeed(fSpeed); 
      if (distance > 0 && distance < 40 || (frontInfra<30 && frontInfra >0) )
     {
       autoTurnRight();
     }
  } 
    if (leftInfra < 25 && leftInfra > 0 && distance < 200 && distance >0)
     {
       //this method used for when the front obstacle is not close enought to the trigger amount but still we get close to the side obstacle,
       // so it turn more to avoide hitting the side obstacle
       rotateOnSpot(rDeg,rotateSpeed);
       delay(300);
       car.setSpeed(fSpeed);
        if (distance > 0 && distance < 40  || (frontInfra<30 && frontInfra >0))
     {
       autoTurnRight();
     }
     }
     else if (rightInfra < 25 && rightInfra > 0 && distance < 200 && distance > 0)
     { 

     //this method used for when the front obstacle is not close enought to the trigger amount but still we get close to the side obstacle,
       // so it turn more to avoide hitting the side obstacle.
         rotateOnSpot(lDeg,rotateSpeed);
        delay(300);
        car.setSpeed(fSpeed);
         if (distance > 0 && distance < 40  || (frontInfra<30 && frontInfra >0))
     {
       autoTurnLeft();
     }
        
      }
      else if (rightInfra < 15 && rightInfra > 0 && distance == 0)
     { 
     //this method used after turning to avoid front obstacle but still we are close to the side obstacle but we want to get as close as possible
     //to side obstacle to grab more trashes without hitting it.
         rotateOnSpot(lDeg,fSpeed);
        delay(200);
        car.setSpeed(fSpeed);
         if (distance > 0 && distance < 40  || (frontInfra<30 && frontInfra >0))
     {
       autoTurnLeft();
     }
      }
     else if (leftInfra < 15 && leftInfra > 0 && distance == 0)
     {
     //this method used after turning to avoid front obstacle but still we are close to the side obstacle but we want to get as close as possible
     //to side obstacle to grab more trashes without hitting it.
         rotateOnSpot(rDeg,rotateSpeed);
       delay(200);
       car.setSpeed(fSpeed);
        if (distance > 0 && distance < 40  || (frontInfra<30 && frontInfra >0))
     {
       autoTurnRight();
     }
     
     }
} 
