import 'package:flutter/material.dart';

import '../widget_utils.dart';

class ButtonsGroupCard extends StatelessWidget
{
  final String groupTitle;
  final String firstButtonTitle;
  final String secondButtonTitle;
  final Function firstButtonOnPressed;
  final Function secondButtonOnPressed;

  const ButtonsGroupCard({
    @required this.groupTitle,
    @required this.firstButtonTitle,
    @required this.secondButtonTitle,
    @required this.firstButtonOnPressed,
    @required this.secondButtonOnPressed,
    Key key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context)
  {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        // Title
        Text(
          groupTitle,
          style: TextStyle(
              color: Colors.grey.shade800,
              fontSize: 18,
              fontWeight: FontWeight.bold
          ),
        ),
        SizedBox(height: 5,),
        Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            // First Button
            MaterialButton(
              color: Colors.blueAccent,
              height: screenAwareSize(60, context),
              child: Text(
                firstButtonTitle,
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 24,
                    fontWeight: FontWeight.bold
                ),
              ),
              onPressed: firstButtonOnPressed,
            ),
            // Second Button
            MaterialButton(
              color: Colors.blueAccent,
              height: screenAwareSize(60, context),
              child: Text(
                secondButtonTitle,
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 24,
                    fontWeight: FontWeight.bold
                ),
              ),
              onPressed: secondButtonOnPressed,
            ),
          ],
        ),
      ],
    );
  }
}