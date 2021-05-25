package com.example.firstapp.activity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.mqtt.MqttHandler
import com.example.firstapp.R


class PopUpWindow : AppCompatActivity() {

    private var mMqttHandler: MqttHandler? = null

  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pop_up_window)

        val mCameraView : ImageView = findViewById(R.id.camera)

        mMqttHandler = MqttHandler(this.applicationContext, mCameraView)
        mMqttHandler!!.connectToMqttBroker()
    }
}