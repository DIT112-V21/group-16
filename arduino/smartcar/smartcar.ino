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

// Speed and angle variables
const int fSpeed   = 25;  // 25% of the full speed forward
const int bSpeed   = -40; // 40% of the full speed backward
const int rotateSpeed = 70 ;  //speed for rotate on spot
int currentSpeed = 0;
const int lDe = -90; // degrees to turn left on spot
const int rDe = 90; // degrees to turn right on spot
const int lDeg= -40;  //Degree for rotate on spot
const int rDeg = 40;  //Degree for rotate on spot

const auto oneSecond = 1000UL;
const auto pulsePerMeter = 600;

//Variables for the vacuum bag
float maxTraveledDistance=0.0;
int bagCapacity=99;
bool bagFull=false;
int bagContents=0;

//Variables for pattern methods
int lengthToTravel = 0;
int pattern = 0;
double area = 0;
int velocity = 0;
const int sideDistance = 10;

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

std::vector<char> frameBuffer;

void setup() {
    Serial.begin(9600);
    #ifdef __SMCE__
    Camera.begin(QVGA, RGB888, 0); //qvga is a format 320 X 240, QVGA, RGB888, 0
    frameBuffer.resize(Camera.width() * Camera.height() * Camera.bytesPerPixel());
    mqtt.begin(WiFi);
    #else
    mqtt.begin(net);
    #endif
    if (mqtt.connect("arduino", "public", "public")) {
        mqtt.subscribe("/smartcar/group16/#", 1);
        mqtt.onMessage(+[](String& topic, String& message) {
        if (topic == "/smartcar/group16/control/throttle") {

            car.setSpeed(message.toInt());
			currentSpeed = message.toInt();
        }
        else if (topic == "/smartcar/group16/control/steering") {
            car.setAngle(message.toInt());
        }
        else if (topic == "/smartcar/group16/auto/size") {
            area = message.toInt() * 100;
        }
        else if (topic=="/smartcar/group16/auto/speed"){
            velocity = message.toInt();
        }
        else if (topic == "/smartcar/group16/auto/pattern"){
            pattern = message.toInt();
        }
        else {
            //Serial.println(topic + " " + message);
        }
    });
  }
}

void loop() {
handlePatterns();
obstacleAvoidance();
  if (mqtt.connected()) {
    mqtt.loop();
    const auto currentTime = millis();
#ifdef __SMCE__
    static auto previousFrame = 0UL;
    if (currentTime - previousFrame >= 65) {
      previousFrame = currentTime;
      Camera.readFrame(frameBuffer.data());
      mqtt.publish("/smartcar/group16/camera", frameBuffer.data(), frameBuffer.size(),false, 0);
    }
#endif
    static auto previousTransmission = 0UL;
    if (currentTime - previousTransmission >= oneSecond) {
      previousTransmission = currentTime;
	  mqtt.publish("/smartcar/group16/distance", String(distanceInMeter()));
	        mqtt.publish("/smartcar/group16/obstacleMsg", String(obstacleDetectionMessage()));
            mqtt.publish("/smartcar/group16/bagfull",String(bagFilledProgress()));

    }
    car.update();

  }
#ifdef __SMCE__
  // Avoid over-using the CPU if we are running in the emulator
  delay(35);
#endif
}

//obstacle avoidance, used in loop
void obstacleAvoidance(){
    unsigned int distance = front.getDistance();
    unsigned int triggerDist = 100;
    const int sideTriggerDist = 30;
    unsigned int frontInfra = frontSensor.getDistance();
    unsigned int leftInfra = leftSensor.getDistance();
    unsigned int rightInfra = rightSensor.getDistance();
    unsigned int backInfra = backSensor.getDistance();

	if (distance > 0 && distance < triggerDist && currentSpeed >= 0 ||   leftInfra < sideTriggerDist && leftInfra > 0 || rightInfra < sideTriggerDist && rightInfra > 0 || backInfra < sideTriggerDist && backInfra > 0  ){ //if there is an obstacle in front of the car and another one at the left side it will turn right.
        car.setSpeed(0);
    }

}

// turns the car, used in obstacleAvoidance, PatternA and PatternB
void turnCar(int Angle, bool moving){
     car.setSpeed(fSpeed);
     currentSpeed = fSpeed;
     car.setAngle(Angle);
     delay(6000);
     car.setAngle(0);
     if (!moving){
         stopVehicle();
     }
}

// Code guide from Dimitris, used in rotation
void rotateOnSpot(int targetDegrees, int speed){
    speed = smartcarlib::utils::getAbsolute(speed);
    targetDegrees %= 360;                   // put it on a (-360,360) scale
    if (!targetDegrees){                   // if the target degrees is 0, don't bother doing
        return;                                 // anything
    }
    if (targetDegrees > 0){                     // positive value means we should rotate clockwise
        car.overrideMotorSpeed(speed, -speed);
    }
    else{                                       // rotate counter clockwise
        car.overrideMotorSpeed(-speed,speed);
    }
    const auto initialHeading = car.getHeading(); // the initial heading we'll use as offset to calculate the absolute displacement
    int degreesTurnedSoFar= 0;                  // this variable will hold the absolute displacement from the beginning of the rotation
    while (abs(degreesTurnedSoFar) < abs(targetDegrees)){ // while absolute displacement hasn't reached the (absolute) target, keep turning
        car.update();                           // update to integrate the latest heading sensor readings
        auto currentHeading = car.getHeading(); // in the scale of 0 to 360
        if ((targetDegrees < 0) && (currentHeading > initialHeading)){    // if we are turning left and the current heading is larger than the
            currentHeading -= 360;                                       // initial one (e.g. started at 10 degrees and now we are at 350), we need to substract
        }                                                                // 360, so to eventually get a signed displacement from the initial heading (-20)
        else if ((targetDegrees > 0) && (currentHeading < initialHeading)){ // if we are turning right and the heading is smaller than the
            currentHeading += 360;                                          // initial one (e.g. started at 350 degrees and now we are at 20), so to get a signed
        }                                                                    // displacement (+30)
        degreesTurnedSoFar = initialHeading - currentHeading;   // degrees turned so far is initial heading minus current
    }                                                           // (initial heading is at least 0 and at most 360. To handle the "edge" cases we substracted or added 360
    car.setSpeed(0); // we have reached the target, so stop the car
}

//Swtich to execute the cleaning patterns
void handlePatterns(){
    switch (pattern){
        case 1:
            zigzagCleaning();
            pattern = 0;
            break;
        case 2:
            inwardCleaning();
            pattern = 0;
            break;
        default:
            break;
    }
}

// Cleaning methods thats being called in handlePatters()
void zigzagCleaning(){
    lengthToTravel = sqrt(area);
    if (lengthToTravel%2==0){
        int nrOfIterations = (lengthToTravel/sideDistance)/2;
        int i =0;
        while (i < nrOfIterations){
            patternB();
            i += 1;
        }
    }
    else {
        int nrOfIterations = (lengthToTravel/sideDistance)/2;
        int j =0;
        while (j < nrOfIterations){
            patternB();
            j += 1;
        }
        patternA();
    }
}

void inwardCleaning(){
  lengthToTravel = sqrt(area);
    int x= (lengthToTravel / sideDistance);
  int y= x/2;
    int z=y-1;
    int i =0;
    if (lengthToTravel%2 ==0){
        while (i<z){
        complete();
        i++;
        }
        goAndRight3();
    }
    else {
        while (i<y){
            complete();
            i++;
        }
        go(sideDistance,fSpeed);

    }
  bagFilledProgress();
}

//two methods thats used in the cleaning pattern methods
void patternA(){
    go(lengthToTravel,velocity);
    turnCar(rDe, false);
  bagFilledProgress();
    go(sideDistance ,fSpeed);
  bagFilledProgress();
    turnCar(rDe, false);
  bagFilledProgress();
}

void patternB(){
    patternA();
    go(lengthToTravel,velocity);
  bagFilledProgress();
    turnCar(lDe, false);
  bagFilledProgress();
    go(sideDistance,fSpeed);
  bagFilledProgress();
    turnCar(lDe, false);
}

// used in patternA, patternB and goAndRight3
void go(double centimeters, int speed){
    if (centimeters == 0){
        return;
    }
    // Ensure the speed is towards the correct direction
    speed = smartcarlib::utils::getAbsolute(speed) * ((centimeters < 0) ? -1 : 1);
    car.setAngle(0);
    car.setSpeed(speed);
    double initialDistance = car.getDistance();
    bool hasReachedTargetDistance = false;
    while (!hasReachedTargetDistance){
        car.update();
        auto currentDistance   = car.getDistance();
        auto travelledDistance = initialDistance > currentDistance
                                     ? initialDistance - currentDistance
                                     : currentDistance - initialDistance;
        hasReachedTargetDistance = travelledDistance >= smartcarlib::utils::getAbsolute(centimeters);
    }
    car.setSpeed(0);
}

// used in inwardCleaning()
void complete(){
    goAndRight3();
    lengthToTravel -=sideDistance;
    go(lengthToTravel,velocity);
}

// used in complete and patternB
void goAndRight3(){
    int i =0;
    while (i<3){
        go(lengthToTravel,velocity);
        delay(500);
        turnCar(rDe, false);
        i++;
    }
}

// Changing odometer measurement from cm to m
int distanceInMeter(){
    int distance = car.getDistance();
    distance = distance/100;
    return distance;
}

String obstacleDetectionMessage(){
    String msg = "obstacle";
    unsigned int triggerDist = 200;
    unsigned int distance = front.getDistance();
    if (distance > 0 && distance < triggerDist && currentSpeed > 0){
        return msg;
    }else{
      return "";
    }
   }


void stopVehicle(){
    car.setSpeed(0);
    currentSpeed = 0;
}

int bagFilledProgress(){ //when car drive 2m, 1% bag filled  //
    int traveledDistance=distanceInMeter();
    if (traveledDistance>maxTraveledDistance){ //only vacuum when vehicle is moving forward
        maxTraveledDistance==traveledDistance;
        if (!bagFull){
            bagContents = (traveledDistance%100)/2;
            Serial.println("Bag is " + (String)bagContents + "% full");
            if (bagContents >= bagCapacity){
                bagFull=true;
            }
            return bagContents;
        }
    }
}