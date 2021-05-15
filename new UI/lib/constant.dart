import 'package:flutter/material.dart';
import 'extentions/mqtt/mqtt_handler.dart';

MQTTHandler mqttHandler;

Size screenSize;
double screenWidth = 0;
double screenHeight = 0;

bool carStatus = false;
bool cameraStatus = true;

bool speedIsPositive = true;
String currentJoyStickAngle = '';
double currentJoyStickSpeed = 0;
String backwardSpeed = '-30';
int currentSpeed = 0;
int startSpeed = 30;
int minSpeed = 10;
int maxSpeed = 100;
int highSpeed = 80;
int normalSpeed = 50;
int lowSpeed = 20;

int currentAngle = 0;
