import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:remote_car/pages/automode_page.dart';
import 'package:remote_car/pages/manualmode_page.dart';
import 'package:remote_car/pages/selectmode_page.dart';

class App extends StatelessWidget
{
  @override
  Widget build(BuildContext context)
  {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Remote Car',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
        // textTheme: GoogleFonts.patrickHandScTextTheme()
      ),
      // initialRoute: initialPage,
      // initialRoute: 'main_page',
      // initialRoute: 'automode_page',
      initialRoute: 'selectmode_page',
      routes:
      {
        SelectModePage.id: (context) => SelectModePage(),
        ManualModePage.id: (context) => ManualModePage(),
        AutoModePage.id: (context) => AutoModePage(),
        // BMIPage.id: (context) => BMIPage(steps: {},),
        // BMRPage.id: (context) => BMRPage(),
        // ResultPage.id: (context) => ResultPage(result: {}, basicData: {}, userInfo: {},),
      },
    );
  }
}
