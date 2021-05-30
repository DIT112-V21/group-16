# Group-16: TDM ![example workflow](https://github.com/DIT112-V21/group-16/actions/workflows/android_build.yml/badge.svg) ![example workflow](https://github.com/DIT112-V21/group-16/actions/workflows/arduino-build.yml/badge.svg)

![0266c5f1-654f-4bfe-bc7d-9fc117f1bda3 sketchpad (7)](https://user-images.githubusercontent.com/72136631/119741302-ba45da80-be85-11eb-8c90-73dac9fc8040.png)

## Contents
* [Product Description](https://github.com/DIT112-V21/group-16/blob/Update_readme/README.md#product-description)
* [Demo Video](https://github.com/DIT112-V21/group-16/blob/Update_readme/README.md#demo-video)
* [Technical Information](https://github.com/DIT112-V21/group-16/blob/Update_readme/README.md#technical-information)
* [Resources](https://github.com/DIT112-V21/group-16/blob/Update_readme/README.md#resources)
* [Installation](https://github.com/DIT112-V21/group-16/blob/Update_readme/README.md#installation)
* [Get Started](https://github.com/DIT112-V21/group-16/blob/Update_readme/README.md#get-started)
* [Guide for Developers](https://github.com/DIT112-V21/group-16/blob/Update_readme/README.md#guide-for-open-source-developers)
* [User Manual](https://github.com/DIT112-V21/group-16/blob/Update_readme/README.md#user-manual)
* [Development Team](https://github.com/DIT112-V21/group-16/blob/Update_readme/README.md#development-team)

## Product Description 
TDM is an automated cleaning car controlled by an android app. The main idea to develop this product is to make cleaning issues automated and during tough times and sleeping hours run the cleaning process without struggling. By using our product the user can choose between two existing modes: manual/autonomous cleaning. In manual mode the user has the possibility of driving the vehicle around with the option of enabling/disabling the vacuum cleaning functionality, as well as observing the cleaning vehicle through a streaming service which is displayed on the android app. While the vehicle is ran autonomously, the user can set the speed and choose to clean any size of area from a two set pre-programmed patterns.

For more a detailed product description, follow the link [Product Description](https://github.com/DIT112-V21/group-16/wiki/Product-Description) to our wiki page. 

 Update_readme
### Features 
* [Manual Cleaning](https://github.com/DIT112-V21/group-16/wiki/Manual-cleaning)
* [Car "Cleaning" Movements](https://github.com/DIT112-V21/group-16/wiki/Car-Cleaning-Movement)
* [TDM App](https://github.com/DIT112-V21/group-16/wiki/TDM-App)
* [Obstacle avoidance](https://github.com/DIT112-V21/group-16/wiki/Obstacle-avoidance-feature)
* [App Connectivity](https://github.com/DIT112-V21/group-16/wiki/TDM-Connectivity-app-feature)
* [Finalize project](https://github.com/DIT112-V21/group-16/wiki/Finalize-Project)
* [Continuous integration](https://github.com/DIT112-V21/group-16/wiki/Continuous-integration)

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
 master

### Demo Video
<img width="502" alt="yh" src="https://user-images.githubusercontent.com/72136631/120119441-ff288480-c197-11eb-8410-c96368fb4771.png">

Click on the following link to view the [Demo](https://www.youtube.com/watch?v=Qwjv83w49fE)

## Technical information
TDM is a cleaning smartcar which utilizes the available capabilities of the smartcar software library, and is controlled by an Android app. 
TDM is a two-tier system, where request are sent to the Smartcar from the app through a MQTT server. An arduino sketch holds all of the required functionalities of the Smartcar. Also, the camera provided by the SMCE emulator is being used in the implementation of the video streaming function in our android app.

To see the software architecture of TDM, please refer to the [Class Diagram](https://github.com/DIT112-V21/group-16/wiki/Class-Diagram) Wiki page.

### Resources 
- [SMCE](https://github.com/ItJustWorksTM/smce-gd)
- [Android Studio](https://developer.android.com/studio)
- [Arduino IDE](https://www.arduino.cc/en/software)
- [Smartcar shield library](https://www.arduinolibraries.info/libraries/smartcar-shield)
- [GitHub](https://github.com/)
- [Smartcar shield library documentation](https://platisd.github.io/smartcar_shield/index.html)
- [MQTT](https://mosquitto.org/download/) 
- [Virtual joystick](https://github.com/controlwear/virtual-joystick-android)
- [MQTT Implementation](https://github.com/DIT112-V21/smartcar-mqtt-controller)

## Installation
###  Get started 
- For information about how to install and use TDM, follow the link [Get Started](https://github.com/DIT112-V21/group-16/wiki/Installation-Guide) to our wiki page.


### Guide for Open Source Developers
- For information about how to install TDM as an Open Source Developer, follow the link  [Installation  Guide for developers](https://github.com/DIT112-V21/group-16/wiki/Installation-Guide-for-Open-Source-Developers) to our wiki page. 

## User Manual
- For information about how to use and navigate the app, follow the link  [User Manual](https://github.com/DIT112-V21/group-16/wiki/User-Manual) to our wiki page. 

## Development Team 
- [Eun Young Cho](https://github.com/Young799)
- [Mohammad Zandkarimi](https://github.com/Mozand)
- [Jina Dawood](https://github.com/JinaDawood)
- [Alexander Andreasson](https://github.com/gusandalce)
- [Adam Magnus](https://github.com/gusmagadc)
