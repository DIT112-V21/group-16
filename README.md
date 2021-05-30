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

For more a detailed product description including what, why and how, follow the link [Product Description](https://github.com/DIT112-V21/group-16/wiki/Product-Description) to our wiki page. 

### Features 
* [Manual Cleaning](https://github.com/DIT112-V21/group-16/wiki/Manual-cleaning)
* [Car "Cleaning" Movements](https://github.com/DIT112-V21/group-16/wiki/Car-Cleaning-Movement)
* [TDM App](https://github.com/DIT112-V21/group-16/wiki/TDM-App)
* [Obstacle avoidance](https://github.com/DIT112-V21/group-16/wiki/Obstacle-avoidance-feature)
* [App Connectivity](https://github.com/DIT112-V21/group-16/wiki/TDM-Connectivity-app-feature)
* [Finalize project](https://github.com/DIT112-V21/group-16/wiki/Finalize-Project)
* [Continuous integration](https://github.com/DIT112-V21/group-16/wiki/Continuous-integration)

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
