package com.example.firstapp

import android.os.Bundle

import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.MQTT.MqttHandler


class ManualOptionActivity : AppCompatActivity() {
    private var mqttHandler: MqttHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val message = findViewById<TextView>(R.id.message)
        setContentView(R.layout.activity_manual_option)

        //mqtt car handler
        mqttHandler = MqttHandler(this.applicationContext)
        mqttHandler!!.connectToMqttBroker(message)

        /* ImageButton cameraButton = findViewById(R.id.camera);
        cameraButton.setOnClickListener { v ->
            popUpWindow = PopUpWindow()
        popUpWindow.showPopupWindow(v)
        }*/
    }
}
