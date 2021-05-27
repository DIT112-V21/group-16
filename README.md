# Group-16: TDM ![example workflow](https://github.com/DIT112-V21/group-16/actions/workflows/android_build.yml/badge.svg) ![example workflow](https://github.com/DIT112-V21/group-16/actions/workflows/arduino-build.yml/badge.svg)

![0266c5f1-654f-4bfe-bc7d-9fc117f1bda3 sketchpad (7)](https://user-images.githubusercontent.com/72136631/119741302-ba45da80-be85-11eb-8c90-73dac9fc8040.png)

## Contents

## Description 

TDM is a cleaning Smartcar controlled by an app. When using our product, the user can choose between two modes: Clean Manually or clean a certain area autonomously. In the manual cleaning mode, the user can drive around and area with the option to turn on/off the vacuum cleaner function. In the autonomous cleaning mode, the user can choose to clean any size of area from two set of pre-programmed patterns. TDM is a two-tier system that inlcudes an Android app and SmartCar, where request are sent to the Smartcar from the app through a MQTT broker. To see the software architecture of TDM, please refer to the [Class Diagram](https://github.com/DIT112-V21/group-16/wiki/Class-Diagram) Wiki page.

### What we are going to make.
- We are going to make an autonomous driven vehichle witht the purpose of cleaning in dorr areas. The vehicle will have a manual controlled option that implements obstacle avoidance and a joystick to manouver with. The automated controll function will feature 2 "Cleaning Pattern" they are a zig zag type pattern and a square pattern allowing the user to adapt the automation to what is best suited for their envoirnement. Furthermore the user can input the size field of the area they wish to clean as well as the velocity. 
- The vehicle will be controlled through an android app connected through an mqtt broker. 

### Why we will make it. 
- We will create a trash disposing vehicle with the purpose of mitigating human labour and making cleaning commersial or outdoor areas easier to clean.
- We want it to be a complementary to the many other traditional methods use which are often not as good for the environment.

### How we are going to make it. 
- The vechicle will be able to clean an area based on a path. 
- The vehicle will utilize a cleaning function to simulate picking up trash.
- The bag will take distance into account when deciding it is filled up. 
- The user will be able to control the vehicle through the SMCE emulator by using an android appplication designed for controlling the vehicle. 
- It will also be possible to take manual control of the vehicle by using a joystick.'
- The project uses c++, and kotlin as source code. 

### Demo Video

## Installation Guide for Open Source Developers

### Smartcar

### Android app 

## User Manual

## Resources
### Software
- [SMCE](https://github.com/ItJustWorksTM/smce-gd)
- [Android Studio](https://developer.android.com/studio)
- [Arduino IDE](https://www.arduino.cc/en/software)
- [Smartcar shield library](https://www.arduinolibraries.info/libraries/smartcar-shield)
- [GitHub](https://github.com/)

## Development Team 
- [Eun Young Cho](https://github.com/Young799)
- [Mohammad Zandkarimi](https://github.com/Mozand)
- [Jina Dawood](https://github.com/JinaDawood)
- [Alexander Andreasson](https://github.com/gusandalce)
- [Adam Magnus](https://github.com/gusmagadc)
