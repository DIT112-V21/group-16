package com.example.firstapp
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.MQTT.MqttHandler


class PopUpWindow : AppCompatActivity() {

     private var mqttHandler: MqttHandler? = null

  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pop_up_window)

        val mCameraView : ImageView = findViewById(R.id.cameraView)

        mqttHandler = MqttHandler(this.applicationContext, mCameraView)
        mqttHandler!!.connectToMqttBroker()


    }
}