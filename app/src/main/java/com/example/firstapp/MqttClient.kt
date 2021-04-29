package com.example.firstapp
import android.content.Context;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;

class MqttClient(context:Context?,
                 serverUrl:String,
                 clientId:String = "") {
    private var mMqttClient = MqttAndroidClient(context, serverUrl, clientId)


    fun connect(connectionCallback:IMqttActionListener,
                clientCallback:MqttCallback) {
        mMqttClient.setCallback(clientCallback)
        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true
        options.isCleanSession = true
        try
        {
            mMqttClient.connect(options, null, connectionCallback)
        }
        catch (e:MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect(disconnectionCallback: IMqttActionListener?) {
        try {
            mMqttClient.disconnect(null, disconnectionCallback)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String?, qos: Int, subscriptionCallback: IMqttActionListener?) {
        try {
            mMqttClient.subscribe(topic, qos, null, subscriptionCallback)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun unsubscribe(topic: String?, unsubscriptionCallback: IMqttActionListener?) {
        try {
            mMqttClient.unsubscribe(topic, null, unsubscriptionCallback)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic: String?, message: String, qos: Int, publishCallback: IMqttActionListener?) {
        val mqttMessage = MqttMessage()
        mqttMessage.payload = message.toByteArray()
        mqttMessage.qos = qos
        try {
            mMqttClient.publish(topic, mqttMessage, null, publishCallback)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}


