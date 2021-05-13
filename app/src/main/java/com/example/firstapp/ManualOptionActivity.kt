package com.example.firstapp
import android.R.attr
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.MQTT.MqttHandler
import io.github.controlwear.virtual.joystick.android.JoystickView


class ManualOptionActivity : AppCompatActivity() {

    private var mqttHandler: MqttHandler? = null
    private var mCameraButton : ImageButton? = null

    private val THROTTLE_CONTROL = "/smartcar/group16/control/throttle"
    private val STEERING_CONTROL = "/smartcar/group16/control/steering"
    private val QOS = 0
    private val FORWARD_SPEED = 40
    private val ANGLE = 0

    private var forwardBtn: Button? = null
    private var backwardBtn: Button? = null
    private var stopBtn: Button? = null
    private var leftBtn: Button? = null
    private var rightBtn: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_option)

        val  mTraveledDistance : TextView = findViewById(R.id.distance)

        val actionBar = supportActionBar
        actionBar!!.title = ""

        //mqtt car handler
        mqttHandler = MqttHandler(this.applicationContext, mTraveledDistance)
        mqttHandler!!.connectToMqttBroker()

        // transition to the popup window when clicking on the camera button
        mCameraButton = findViewById(R.id.camera)
        mCameraButton?.setOnClickListener {
            val window = PopupWindow(this.applicationContext)
            val view = layoutInflater.inflate(R.layout.pop_up_window, null)
            window.contentView = view
            val imageView = view.findViewById<ImageView>(R.id.cameraView)
            imageView.setOnClickListener{
                window.dismiss()
            }
            window.showAsDropDown(mCameraButton)
        }

        // this joystick is adapted from: https://github.com/controlwear/virtual-joystick-android
        val joystickLeft = findViewById<View>(R.id.joystickView_left) as JoystickView
        joystickLeft.setOnMoveListener { angle, strength ->

        }

    }



  /*  fun forward(view: View) {
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
    }*/
}
