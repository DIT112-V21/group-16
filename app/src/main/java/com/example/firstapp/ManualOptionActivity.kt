package com.example.firstapp
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.MQTT.MqttHandler
import io.github.controlwear.virtual.joystick.android.JoystickView
import io.github.controlwear.virtual.joystick.android.JoystickView.OnMoveListener


class ManualOptionActivity : AppCompatActivity() {

    private var mqttHandler: MqttHandler? = null
    private var mCameraButton : ImageButton? = null

    private val REVERSE = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_option)

        val  mTraveledDistance : TextView = findViewById(R.id.distance)
        val  mSpeed: TextView = findViewById(R.id.speed)
        val  mFront : TextView = findViewById(R.id.front)

        val actionBar = supportActionBar
        actionBar!!.title = ""

        //mqtt car handler
        mqttHandler = MqttHandler(this.applicationContext, mTraveledDistance, mSpeed, mFront)
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

        // This joystick is adapted from: https://github.com/controlwear/virtual-joystick-android
        val joystick = findViewById<View>(R.id.joystickView_left) as JoystickView
        joystick.setOnMoveListener(object : OnMoveListener {
            var currentSpeed = 0
            var currentAngle = 0
            override fun onMove(angle: Int, strength: Int) {
                var newSpeed: Int
                var newAngle: Int
                if (angle in 90..180) {
                    newAngle = turnForwards(angle)
                    newSpeed = (strength * 0.8).toInt()
                } else if (angle in 0..89) {
                    newAngle = turnForwards(angle)
                    newSpeed = (strength * 0.8).toInt()
                } else if (angle > 0 && angle >= 270) {
                    newAngle = turnBackwards(angle)
                    newSpeed = (strength * 0.5 * REVERSE).toInt()
                } else {
                    newAngle = turnBackwards(angle)
                    newSpeed = (strength * 0.5 * REVERSE).toInt()
                }
                if (newAngle != currentAngle || newSpeed != currentSpeed) {
                    if (newSpeed == 0) newAngle = 0
                    sendMovement(newSpeed, newAngle)
                    currentAngle = newAngle
                    currentSpeed = newSpeed
                }
            }
        })
    }
    private fun turnForwards ( angle: Int) : Int{
        val angle = 90 - angle
        return angle
    }

    private fun turnBackwards ( angle: Int) : Int{
        val angle =  angle - 270
        return angle
    }

    private fun sendMovement(newSpeed: Int, newAngle: Int) {
            mqttHandler?.drive(newSpeed,newAngle,"")
        }
     }