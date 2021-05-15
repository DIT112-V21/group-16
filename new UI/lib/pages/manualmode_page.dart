import 'dart:async';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter_switch/flutter_switch.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:remote_car/constant.dart';
import 'package:remote_car/extentions/mqtt/mqtt_handler.dart';
import 'package:remote_car/widgets/cards/custom_list_tile.dart';
import 'package:remote_car/widgets/widget_utils.dart';
import 'package:control_pad/views/joystick_view.dart';
import 'package:video_player/video_player.dart';

import '../constant.dart';
import '../constant.dart';
import '../constant.dart';
import '../constant.dart';
import '../constant.dart';
import '../constant.dart';
import '../constant.dart';
import '../constant.dart';
import '../constant.dart';
import '../constant.dart';
import '../constant.dart';

class ManualModePage extends StatefulWidget {
  static const String id = 'main_page';
  const ManualModePage({Key key}) : super(key: key);

  @override
  _ManualModePageState createState() => _ManualModePageState();
}

class _ManualModePageState extends State<ManualModePage> {
  VideoPlayerController _videoPlayerController;
  Future<void> _initVideoPlayerFuture;

  void setupConnection() async {
    if (mqttHandler == null) mqttHandler = MQTTHandler();

    if (mqttHandler.isConnected == false) {
      await mqttHandler.connect();
      mqttHandler.publish(
          topic: "/smartcar/group16/control/throttle",
          message: currentSpeed.toString());
    }
  }

  @override
  void initState() {
    // TODO: Fix bugs
    // _videoPlayerController = VideoPlayerController.network(
    //     'https://videos1.varzeshe3.com/videos/2021/05/13/D/aba059db-446f-448b-a70e-abbf9de7e664.mp4');
    // _initVideoPlayerFuture = _videoPlayerController.initialize();
    // _videoPlayerController.setLooping(false);
    // _videoPlayerController.setVolume(1.0);
    // _videoPlayerController.play();

    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    setupConnection();

    if (screenWidth == 0 && screenHeight == 0) {
      screenSize = MediaQuery.of(context).size;
      screenWidth = screenSize.width;
      screenHeight = screenSize.height;
      // print('screenHeight: ${screenHeight}, screenWidth: ${screenWidth}');
    }

    return Scaffold(
      // appBar: AppBar(
      //
      // ),
      body: SafeArea(
        child: Row(
          children: [
            Container(
              // color: Colors.grey.shade400,
              width: screenAwareSize(300.0, context),
              height: double.maxFinite,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  // Car Status
                  Padding(
                    padding: EdgeInsets.only(
                        left: screenAwareSize(5.0, context),
                        right: screenAwareSize(5.0, context)),
                    child: Row(
                      children: [
                        Text(
                          'Car Status:',
                          style: TextStyle(
                              fontSize: 18, fontWeight: FontWeight.bold),
                        ),
                        Spacer(),
                        FlutterSwitch(
                            width: screenAwareSize(125, context),
                            height: screenAwareSize(60, context),
                            toggleSize: screenAwareSize(40, context),
                            valueFontSize: screenAwareSize(28, context),
                            duration: Duration(milliseconds: 800),
                            showOnOff: true,
                            // activeText: 'ON',
                            // inactiveText: 'OFF',
                            value: carStatus,
                            onToggle: (val) {
                              setState(() {
                                carStatus = val;
                                currentSpeed = 0;
                                if (carStatus) {
                                  currentSpeed = startSpeed;
                                }

                                // mqttHandler.publish(
                                //     topic: "/smartcar/group16/control/throttle",
                                //     message: currentSpeed.toString());
                              });
                            }),
                      ],
                    ),
                  ),
                  // Speed Mode
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      // HIGH
                      CustomListTile(
                        toolTip: 'Set Speed to HIGH',
                        leading: Icon(
                          Icons.circle,
                          color: Colors.red,
                          size: 30,
                        ),
                        title: 'HIGH',
                        titleStyle: TextStyle(
                            fontSize: 16,
                            color: Colors.grey.shade800,
                            fontWeight: FontWeight.bold),
                        // subTitle: 'Sub',
                        // subTitleStyle: TextStyle(
                        //     fontSize: 14,
                        //     color: Colors.grey.shade600,
                        //     fontWeight: FontWeight.w800),
                        onTap: () {
                          print('HIGH');
                          if (carStatus) {
                            currentSpeed = highSpeed;
                            // mqttHandler.publish(
                            //     topic: "/smartcar/group16/control/throttle",
                            //     message: currentSpeed.toString());
                          }
                        },
                      ),
                      // NORMAL
                      CustomListTile(
                        toolTip: 'Set Speed to NORMAL',
                        leading: Icon(
                          Icons.circle,
                          color: Colors.green,
                          size: 30,
                        ),
                        title: 'NORMAL',
                        titleStyle: TextStyle(
                            fontSize: 16,
                            color: Colors.grey.shade800,
                            fontWeight: FontWeight.bold),
                        // subTitle: 'Sub',
                        // subTitleStyle: TextStyle(
                        //     fontSize: 14,
                        //     color: Colors.grey.shade600,
                        //     fontWeight: FontWeight.w800),
                        onTap: () {
                          print('NORMAL');
                          if (carStatus) {
                            currentSpeed = normalSpeed;
                            // mqttHandler.publish(
                            //   topic: "/smartcar/group16/control/throttle",
                            //   message: currentSpeed.toString(),
                            // );
                          }
                        },
                      ),
                      // LOW
                      CustomListTile(
                        toolTip: 'Set Speed to LOW',
                        leading: Icon(
                          Icons.circle,
                          color: Colors.orange,
                          size: 30,
                        ),
                        title: 'LOW',
                        titleStyle: TextStyle(
                            fontSize: 16,
                            color: Colors.grey.shade800,
                            fontWeight: FontWeight.bold),
                        // subTitle: 'Sub',
                        // subTitleStyle: TextStyle(
                        //     fontSize: 14,
                        //     color: Colors.grey.shade600,
                        //     fontWeight: FontWeight.w800),
                        onTap: () {
                          print('LOW');
                          if (carStatus) {
                            currentSpeed = lowSpeed;
                            // mqttHandler.publish(
                            //     topic: "/smartcar/group16/control/throttle",
                            //     message: currentSpeed.toString());
                          }
                        },
                      ),
                      // Title
                      Center(
                        child: Text(
                          'Speed Mode',
                          style: TextStyle(
                              fontSize: 16, fontWeight: FontWeight.bold),
                        ),
                      ),
                    ],
                  ),
                  // Actions
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        mainAxisAlignment: MainAxisAlignment.start,
                        children: [
                          // Increase
                          MaterialButton(
                            color: Colors.blue,
                            elevation: screenAwareSize(10, context),
                            padding:
                                EdgeInsets.all(screenAwareSize(20, context)),
                            shape: CircleBorder(),
                            child: Icon(
                              Icons.add,
                              color: Colors.white,
                              size: 30,
                            ),
                            // splashColor: Colors.blue.shade200,
                            disabledColor: Colors.red,
                            onPressed: () {
                              print('Increase');
                              if (carStatus) {
                                if (currentSpeed + 10 <= maxSpeed) {
                                  currentSpeed += 10;
                                }
                                // mqttHandler.publish(
                                //     topic: "/smartcar/group16/control/throttle",
                                //     message: currentSpeed.toString());
                              }
                            },
                            onLongPress: () {
                              print('Long Increase');
                              if (carStatus) {
                                if (currentSpeed + 20 <= maxSpeed) {
                                  currentSpeed += 20;
                                }
                                // mqttHandler.publish(
                                //     topic: "/smartcar/group16/control/throttle",
                                //     message: currentSpeed.toString());
                              }
                            },
                          ),
                          // Decrease
                          MaterialButton(
                            color: Colors.blue,
                            elevation: screenAwareSize(10, context),
                            padding:
                                EdgeInsets.all(screenAwareSize(20, context)),
                            shape: CircleBorder(),
                            child: Icon(
                              Icons.remove,
                              color: Colors.white,
                              size: 30,
                            ),
                            // splashColor: Colors.blue.shade200,
                            disabledColor: Colors.red,
                            onPressed: () {
                              print('Decrease');
                              if (carStatus) {
                                if (currentSpeed - 10 >= minSpeed) {
                                  currentSpeed -= 10;
                                }
                                // mqttHandler.publish(
                                //     topic: "/smartcar/group16/control/throttle",
                                //     message: currentSpeed.toString());
                              }
                            },
                            onLongPress: () {
                              print('Long Decrease');
                              if (carStatus) {
                                if (currentSpeed - 20 >= minSpeed) {
                                  currentSpeed -= 20;
                                }
                                // mqttHandler.publish(
                                //     topic: "/smartcar/group16/control/throttle",
                                //     message: currentSpeed.toString());
                              }
                            },
                          ),
                        ],
                      ),
                      SizedBox(
                        height: 10,
                      ),
                      Center(
                        child: Text(
                          'Adjust Speed',
                          style: TextStyle(
                              fontSize: 16, fontWeight: FontWeight.bold),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            Expanded(
              child: Container(
                alignment: Alignment.topRight,
                height: double.maxFinite,
                width: double.maxFinite,
                child: Stack(
                  children: [
                    //Realtime Video
                    Container(
                      color: Colors.orange,
                      height: double.maxFinite,
                      width: double.maxFinite,
                      child: Image.network(
                        'https://static2.farakav.com/files/pictures/01600951.jpg',
                        fit: BoxFit.fill,
                      ),
                    ),
                    // AspectRatio(
                    //   aspectRatio: _videoPlayerController.value.aspectRatio,
                    //   child: VideoPlayer(_videoPlayerController),
                    // ),
                    // Car Status
                    Container(
                      alignment: Alignment.topRight,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.end,
                        mainAxisAlignment: MainAxisAlignment.start,
                        children: [
                          // TODO: BUG
                          // FlutterSwitch(
                          //     width: screenAwareSize(125, context),
                          //     height: screenAwareSize(60, context),
                          //     toggleSize: screenAwareSize(40, context),
                          //     valueFontSize: screenAwareSize(28, context),
                          //     duration: Duration(milliseconds: 800),
                          //     showOnOff: true,
                          //     // activeText: 'ON',
                          //     // inactiveText: 'OFF',
                          //     value: cameraStatus,
                          //     onToggle: (val)
                          //     {
                          //       setState(()
                          //       {
                          //         cameraStatus = val;
                          //       });
                          //     }
                          // ),
                          // Text(
                          //   'Camera',
                          //   style: TextStyle(
                          //       fontSize: 16,
                          //       color: Colors.white,
                          //       fontWeight: FontWeight.bold
                          //   ),
                          // ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
            Container(
              color: Colors.green,
              width: screenAwareSize(300.0, context),
              height: double.maxFinite,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  // Car Info
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      // TODO: Add Battery and Map
                      // Battery
                      FaIcon(
                        // Icons.battery_full,
                        FontAwesomeIcons.batteryThreeQuarters,
                        color: Colors.white,
                        size: screenAwareSize(60, context),
                      ),
                      // Map
                      /*FaIcon(
                        // Icons.battery_full,
                        FontAwesomeIcons.mapMarkedAlt,
                        color: Colors.white,
                        size: screenAwareSize(60, context),
                      ),*/
                    ],
                  ),
                  JoystickView(
                      size: screenAwareSize(250, context),
                      onDirectionChanged: (value1, value2) {
                        // print('value1: $value1, value2: $value2, ');

                        // currentJoyStickAngle = value1.toInt();
                        currentJoyStickSpeed = value2;

                        // Forward
                        if (value1 <= 15 || value1 >= 345) {
                          currentJoyStickAngle = '0';
                          speedIsPositive = true;
                        }

                        // TopRight
                        if (value1 >= 15 || value1 < 75) {
                          currentJoyStickAngle = '25';
                          speedIsPositive = true;
                        }

                        // Right
                        if (value1 >= 75 && value1 <= 105) {
                          currentJoyStickAngle = '50';
                          speedIsPositive = true;
                        }

                        // BottomRight
                        if (value1 > 105 || value1 < 165) {
                          currentJoyStickAngle = '25';
                          speedIsPositive = false;
                        }

                        // Backward
                        if (value1 >= 165 && value1 <= 195) {
                          currentJoyStickAngle = '0';
                          speedIsPositive = false;
                        }

                        // BottomLeft
                        if (value1 > 195 || value1 < 240) {
                          currentJoyStickAngle = '-25';
                          speedIsPositive = false;
                        }

                        // Left
                        if (value1 >= 255 && value1 <= 285) {
                          currentJoyStickAngle = '-50';
                          speedIsPositive = false;
                        }

                        // TopLeft
                        if (value1 > 285 || value1 < 345) {
                          currentJoyStickAngle = '-25';
                          speedIsPositive = true;
                        }

                        if (currentJoyStickSpeed >= 0.1) {
                          if (carStatus) {
                            print(currentSpeed);
                            print(backwardSpeed);
                            mqttHandler.publish(
                                topic: "/smartcar/group16/control/steering",
                                message: currentJoyStickAngle);

                            mqttHandler.publish(
                                topic: "/smartcar/group16/control/throttle",
                                message: speedIsPositive
                                    ? currentSpeed.toString()
                                    : backwardSpeed);
                          }
                        } else {
                          currentJoyStickAngle = '0';
                          currentSpeed = 0;

                          mqttHandler.publish(
                              topic: "/smartcar/group16/control/steering",
                              message: currentJoyStickAngle);

                          mqttHandler.publish(
                              topic: "/smartcar/group16/control/throttle",
                              message: currentSpeed.toString());
                        }
                      }),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
