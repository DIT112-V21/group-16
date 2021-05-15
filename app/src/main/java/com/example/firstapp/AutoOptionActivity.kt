package com.example.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupWindow
import com.example.firstapp.MQTT.MqttHandler

class AutoOptionActivity : AppCompatActivity() {
    private var mqttHandler: MqttHandler? = null
    private var mCameraButton : ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_option)

        val actionBar = supportActionBar
        actionBar!!.title = ""

        //mqtt car handler
        mqttHandler = MqttHandler(this.applicationContext)
        mqttHandler!!.connectToMqttBroker()

        // Transition to the popup window when clicking on the camera button
        mCameraButton = findViewById(R.id.camera)
        mCameraButton?.setOnClickListener {
            val window = PopupWindow(this.applicationContext)
            val view = layoutInflater.inflate(R.layout.pop_up_window, null)
            window.contentView = view
            val imageView = view.findViewById<ImageView>(R.id.cameraView)

            mqttHandler = MqttHandler(this.applicationContext, imageView)
            mqttHandler!!.connectToMqttBroker()

            imageView.setOnClickListener{
                window.dismiss()
            }
            window.showAsDropDown(mCameraButton)
        }


    }
}