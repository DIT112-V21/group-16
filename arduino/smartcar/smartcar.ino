

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
int currentSpeed = 0;
int bagStatus=0;
float maxTraveledDistance=0.0;
int bagCapacity=99;
bool bagFull=false;

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
  Serial.begin(3600); //was 9600
 #ifdef __SMCE__
 Camera.begin(QVGA, RGB888, 0); //qvga is a format 320 X 240, QVGA, RGB888, 0
  frameBuffer.resize(Camera.width() * Camera.height() * Camera.bytesPerPixel());
  mqtt.begin("aerostun.dev", 1883, WiFi);
 #else
  mqtt.begin(net);
  #endif
  if (mqtt.connect("arduino", "public", "public")) {
    mqtt.subscribe("/smartcar/group16/control/#", 1);
    mqtt.onMessage([](String topic, String message) {
      if (topic == "/smartcar/group16/control/throttle") {
        currentSpeed = message.toInt();
        car.setSpeed(currentSpeed);
      } else if (topic == "/smartcar/group16/control/steering") {
        car.setAngle(message.toInt());}

      else {
        Serial.println(topic + " " + message);
      }
    });
  }
}

void loop() {
  if(!bagFull){
     obstacleAvoidance();
     Serial.println((leftOdometer.getDistance() + rightOdometer.getDistance())/2);
     bagFilledProgress();


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
    if (currentTime - previousTransmission >= oneSecond) {
      previousTransmission = currentTime;
      const auto distance = String(front.getDistance());
      mqtt.publish("/smartcar/ultrasound/front", distance);
      mqtt.publish("/smartcar/group16/distance", String(car.getDistance()));
      mqtt.publish("/smartcar/group16/speed", String(car.getSpeed()));
      mqtt.publish("/smartcar/group16/bagfull",String(bagFilledProgress()));

    }
  }
#ifdef __SMCE__
  // Avoid over-using the CPU if we are running in the emulator
  delay(35);
#endif
}
}

void driveInASquare (){
  int maxdriveDistance = 400;
  while (maxdriveDistance>0){
    driveDistance(maxdriveDistance);
    turnRight();
    driveDistance(maxdriveDistance);
    turnRight();
    driveDistance(maxdriveDistance);
    turnRight();
    maxdriveDistance--;
  }
}

void driveDistance(int distance){
  car.setSpeed(5);
  while (distance >= (int)car.getDistance()){
  }
  car.setSpeed(0);
}

void turnRight(){
  car.setAngle(90);
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

  if (distance > 0 && distance < triggerDist && currentSpeed >= 0 && leftInfra < sideTriggerDist ){ //third condition added that checks if the car is moving forward.
      autoTurnRight();
  }
  else if(distance > 0 && distance < triggerDist && currentSpeed >= 0 && rightInfra < sideTriggerDist){
     autoTurnLeft();
  }
 else if (distance > 0 && distance < triggerDist && currentSpeed >= 0 ) {
      autoTurnLeft();
  }

   // float traveled_Dis=(leftOdometer.getDistance() + rightOdometer.getDistance())/2
  }

  int bagFilledProgress(){
    float traveledDistance=car.getDistance();
    if (traveledDistance>maxTraveledDistance){
        maxTraveledDistance=traveledDistance;
        int bagContents = (int)((int)traveledDistance%1000)/10;
        if (bagContents == bagCapacity){
          stopVehicle();
          bagFull=true;
          Serial.println("Bag is full. Please change");
        }
        Serial.println("Bag is " + (String)bagContents + "% full");
        return bagContents;
    }
  }

    // initialize progressBar when emptyBag() is invoked, not possible to reset the odometer

     void emptyBag() {
      bool bagFull=false;
      }