# Group-16: TDM ![example workflow](https://github.com/DIT112-V21/group-16/actions/workflows/android_build.yml/badge.svg) ![example workflow](https://github.com/DIT112-V21/group-16/actions/workflows/arduino-build.yml/badge.svg)

![0266c5f1-654f-4bfe-bc7d-9fc117f1bda3 sketchpad (7)](https://user-images.githubusercontent.com/72136631/119741302-ba45da80-be85-11eb-8c90-73dac9fc8040.png)

## Contents

## Product Description 
TDM is an automated cleaning car controlled by an android app. The main idea to develop this product is to make cleaning issues automated and during tough times and sleeping hours run the cleaning process without struggling. By using our product the user can choose between two existing modes: manual/autonomous cleaning. In manual mode the user has the possibility of driving the vehicle around with the option of enabling/disabling the vacuum cleaning functionality, as well as observing the cleaning vehicle through a streaming service which is displayed on the android app. While the vehicle is ran autonomously, the user can set the speed and choose to clean any size of area from a two set pre-programmed patterns.

For more a detailed product description, follow the link [Product Description](https://github.com/DIT112-V21/group-16/wiki/Product-Description) to our wiki page. 

### Demo Video

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
