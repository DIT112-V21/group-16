package com.example.firstapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.MQTT.MqttHandler
import com.example.firstapp.MQTT.MqttClient


class ManualOptionActivity : AppCompatActivity() {

    private var mqttHandler: MqttHandler? = null
    private var mCameraButton : ImageButton? = null

    private var forwardBtn: Button? = null
    private var backwardBtn: Button? = null
    private var stopBtn: Button? = null
    private var leftBtn: Button? = null
    private var rightBtn: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val message = findViewById<TextView>(R.id.message)
        setContentView(R.layout.activity_manual_option)

        //mqtt car handler
        mqttHandler = MqttHandler(this.applicationContext)
        mqttHandler!!.connectToMqttBroker()

        // Pop up window
        mCameraButton = findViewById(R.id.camera)
        mCameraButton?.setOnClickListener {
            val window = PopupWindow(this.applicationContext)
            val view = layoutInflater.inflate(R.layout.pop_up_window, null)
            window.contentView = view
            val imageView = view.findViewById<ImageView>(R.id.camera)
            imageView.setOnClickListener{
                window.dismiss()
            }
            window.showAsDropDown(mCameraButton)
        }
    }



    fun forward(view: View) {
        forwardBtn = findViewById(R.id.forward)
        mqttHandler!!.forward(forwardBtn)
    }
    fun backward(view: View) {
        backwardBtn = findViewById(R.id.backward)
        mqttHandler!!.backward(backwardBtn)
    }
    fun forwardRight(view: View) {
        rightBtn = findViewById(R.id.rightForward)
        mqttHandler!!.forwardRight(rightBtn)
    }
    fun forwardLeft(view: View) {
        leftBtn = findViewById(R.id.leftForward)
        mqttHandler!!.forwardLeft(leftBtn)
    }
    fun stop(view: View) {
        stopBtn = findViewById(R.id.stop)
        mqttHandler!!.stop(stopBtn)
    }

}
