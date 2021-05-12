package com.example.firstapp

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.MQTT.MqttHandler


class PopUpWindow : AppCompatActivity() {

     private var mqttHandler: MqttHandler? = null
     private var mCameraView : ImageView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pop_up_window)

        mqttHandler = MqttHandler(this.applicationContext, mCameraView)
        mqttHandler!!.connectToMqttBroker()

        mCameraView = findViewById(R.id.camera)
    }
}