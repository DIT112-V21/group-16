import 'dart:async';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter_rounded_progress_bar/flutter_rounded_progress_bar.dart';
import 'package:flutter_rounded_progress_bar/rounded_progress_bar_style.dart';
import 'package:flutter_switch/flutter_switch.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:remote_car/constant.dart';
import 'package:remote_car/extentions/mqtt/mqtt_handler.dart';
import 'package:remote_car/widgets/cards/buttons_group_card.dart';
import 'package:remote_car/widgets/cards/custom_list_tile.dart';
import 'package:remote_car/widgets/widget_utils.dart';
import 'package:control_pad/views/joystick_view.dart';
import 'package:video_player/video_player.dart';

import '../widgets/widget_utils.dart';
import '../widgets/widget_utils.dart';
import '../widgets/widget_utils.dart';

class AutoModePage extends StatefulWidget {
  static const String id = 'automode_page';
  const AutoModePage({Key key}) : super(key: key);

  @override
  _AutoModePageState createState() => _AutoModePageState();
}

class _AutoModePageState extends State<AutoModePage> {
  double capacityMin = 0.0;
  double capacityMax = 100.0;
  double capacityValue = 60.0;
  double traveledDistance = 10.5;

  VideoPlayerController _videoPlayerController;
  Future<void> _initVideoPlayerFuture;

  void setupConnection() async {
    if (mqttHandler == null) mqttHandler = MQTTHandler();

    if (mqttHandler.isConnected == false) {
      await mqttHandler.connect();
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
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  // Pattern
                  ButtonsGroupCard(
                    groupTitle: 'Pattern',
                    firstButtonTitle: '1',
                    firstButtonOnPressed: () {},
                    secondButtonTitle: '2',
                    secondButtonOnPressed: () {},
                  ),
                  // Size
                  ButtonsGroupCard(
                    groupTitle: 'Size',
                    firstButtonTitle: '1',
                    firstButtonOnPressed: () {},
                    secondButtonTitle: '2',
                    secondButtonOnPressed: () {},
                  ),
                  // Action
                  ButtonsGroupCard(
                    groupTitle: 'Size',
                    firstButtonTitle: 'Start',
                    firstButtonOnPressed: () {},
                    secondButtonTitle: 'Stop',
                    secondButtonOnPressed: () {},
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
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  SizedBox(
                    height: screenAwareSize(30, context),
                  ),
                  // Bag Capacity
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      Padding(
                        padding: EdgeInsets.only(left: 5),
                        child: Text(
                          'Bag filled status:',
                          style: TextStyle(
                              color: Colors.white,
                              fontSize: 18,
                              fontWeight: FontWeight.w700),
                        ),
                      ),
                      RoundedProgressBar(
                        childCenter: Text(
                          capacityValue.toInt().toString(),
                        ),
                        height: screenAwareSize(40, context),
                        margin: EdgeInsets.all(10),
                        milliseconds: 1000,
                        percent: capacityValue,
                        theme: RoundedProgressBarTheme.yellow,
                        borderRadius: BorderRadius.circular(24),
                      ),
                    ],
                  ),
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      Padding(
                        padding: EdgeInsets.only(left: 5, top: 20, bottom: 10),
                        child: Text(
                          'Traveled Distance:',
                          style: TextStyle(
                              color: Colors.white,
                              fontSize: 18,
                              fontWeight: FontWeight.w700),
                        ),
                      ),
                      Text(
                        '$traveledDistance',
                        style: TextStyle(
                            color: Colors.white,
                            fontSize: 28,
                            fontWeight: FontWeight.bold),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
