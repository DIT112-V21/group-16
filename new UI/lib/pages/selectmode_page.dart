import 'package:flutter/material.dart';
import 'package:remote_car/pages/manualmode_page.dart';

import '../widgets/widget_utils.dart';
import 'automode_page.dart';

class SelectModePage extends StatelessWidget
{
  static const String id = 'selectmode_page' ;

  Size screenSize;
  double screenWidth = 0;
  double screenHeight = 0;

  @override
  Widget build(BuildContext context)
  {

    if (screenWidth == 0 && screenHeight == 0) {
      screenSize = MediaQuery.of(context).size;
      screenWidth = screenSize.width;
      screenHeight = screenSize.height;
      // print('screenHeight: ${screenHeight}, screenWidth: ${screenWidth}');
    }

    return Scaffold(
      body: SafeArea(
        child: Center(
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              // Manual Page
              FlatButton(
                color: Colors.blue,
                minWidth: screenAwareSize(400, context),
                height: screenAwareSize(450, context),
                onPressed: ()
                {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => ManualModePage(),
                    ),
                  );
                },
                child: Text(
                  'Manual Mode',
                  style: TextStyle(
                    fontSize: 28,
                    color: Colors.white,
                    fontWeight: FontWeight.bold
                  ),
                ),
              ),
              // AutoMode Page
              FlatButton(
                color: Colors.blue,
                minWidth: screenAwareSize(400, context),
                height: screenAwareSize(450, context),
                onPressed: ()
                {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => AutoModePage(),
                    ),
                  );
                },
                child: Text(
                  'Auto Mode Mode',
                  style: TextStyle(
                      fontSize: 28,
                      color: Colors.white,
                      fontWeight: FontWeight.bold
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
