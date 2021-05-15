import 'package:flutter/material.dart';
import 'package:remote_car/widgets/widget_utils.dart';

class CustomListTile extends StatelessWidget
{
  final String toolTip;
  final Widget leading;
  final String title;
  final TextStyle titleStyle;
  final String subTitle;
  final TextStyle subTitleStyle;
  final Function onTap;

  // CustomListTile({
  //   this.leading,
  //   this.title,
  //   this.titleStyle,
  //   this.subTitle,
  //   this.subTitleStyle,
  //   Key key,
  // }) : super(key: key);

  CustomListTile({
    this.toolTip,
    this.leading,
    this.title,
    this.titleStyle,
    this.subTitle,
    this.subTitleStyle,
    this.onTap,
  }): assert(toolTip != null),
      assert(title != null),
      assert(leading != null);


  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.all(8.0),
      child: Tooltip(
        message: toolTip,
        child: InkWell(
          onTap: onTap,
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              leading,
              SizedBox(width: screenAwareSize(15, context),),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  // Title
                  Text(
                    title,
                    style: titleStyle,
                  ),
                  subTitle != null
                  ? SizedBox(
                    height: screenAwareSize(5, context),
                  )
                  : SizedBox(),
                  // SubTitle
                  subTitle != null
                      ? Text(
                    subTitle,
                    style: subTitleStyle,
                  )
                      : SizedBox(),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}