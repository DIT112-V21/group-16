package com.example.firstapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.MQTT.MqttHandler
import com.example.firstapp.MQTT.MqttClient


class ManualOptionActivity : AppCompatActivity() {

    private var mqttHandler: MqttHandler? = null
    private var mCamera : ImageButton? = null

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



        mCamera = findViewById<ImageButton>(R.id.camera)
        mCamera?.setOnClickListener{
           val intent = Intent(this,PopUpWindow::class.java)
            startActivity(intent)
        }




       /* ImageButton cameraButton = findViewById(R.id.camera)
        cameraButton.setOnClickListener { v ->
            popUpWindow = PopUpWindow()
        popUpWindow.showPopupWindow(v)

        }*/
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
