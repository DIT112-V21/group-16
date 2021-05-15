import 'dart:typed_data';

import 'package:bitmap/bitmap.dart';
import 'package:mqtt_client/mqtt_client.dart';
import 'package:mqtt_client/mqtt_server_client.dart';
import 'dart:io';

class MQTTHandler {
  MqttServerClient client;
  bool isConnected = false;

  // Future<MqttServerClient> connect() async
  Future<void> connect() async {
    // client = MqttServerClient('aerostun.dev', 'flutter_client');
    client = MqttServerClient('10.0.2.2', 'app');
    client.port = 1883;
    client.setProtocolV311();
    client.logging(on: false);
    client.secure = false;
    client.onConnected = onConnected;
    client.onDisconnected = onDisconnected;
    client.onUnsubscribed = onUnsubscribed;
    client.onSubscribed = onSubscribed;
    client.onSubscribeFail = onSubscribeFail;
    client.pongCallback = pong;

    client.connectionMessage =
        MqttConnectMessage().withClientIdentifier('flutter app').startClean();
    print(client.port);
    print(client.server);
    try {
      await client.connect();
    } catch (e) {
      print('Exception: $e');
      client.disconnect();
    }

    client.updates.listen((List<MqttReceivedMessage<MqttMessage>> c) {
      final MqttPublishMessage message = c[0].payload;
      final payload =
          MqttPublishPayload.bytesToStringAsString(message.payload.message);

      print('Received message:$payload from topic: ${c[0].topic}>');
    });

    // return client;
  }

// connection succeeded
  void onConnected() {
    print('Connected');
    isConnected = true;
  }

  void onDisconnect() {
    client.disconnect();
    isConnected = false;
  }

// unconnected
  void onDisconnected() {
    print('Disconnected');
  }

// subscribe to topic succeeded
  void onSubscribed(String topic) {
    print('Subscribed topic: $topic');
  }

// subscribe to topic failed
  void onSubscribeFail(String topic) {
    print('Failed to subscribe $topic');
  }

// unsubscribe succeeded
  void onUnsubscribed(String topic) {
    print('Unsubscribed topic: $topic');
  }

// PING response received
  void pong() {
    print('Ping response client callback invoked');
  }

  void subscribe({String topic}) {
    // client.subscribe("topic/test", MqttQos.atLeastOnce);
    var a = client.subscribe(topic, MqttQos.atLeastOnce);
    print(a.toString());
  }

  void publish({String topic, String message}) {
    // const pubTopic = 'topic/test';
    // final builder = MqttClientPayloadBuilder();
    // builder.addString('Hello MQTT');
    // client.publishMessage(pubTopic, MqttQos.atLeastOnce, builder.payload);

    final builder = MqttClientPayloadBuilder();
    builder.addString(message);
    client.publishMessage(topic, MqttQos.atLeastOnce, builder.payload);
    print(client.published);
  }

  void unsubscribe({String topic}) {
    client.unsubscribe(topic);
  }
}
