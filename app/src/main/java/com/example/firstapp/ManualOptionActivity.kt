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

    private val THROTTLE_CONTROL = "/smartcar/group16/control/throttle"
    private val STEERING_CONTROL = "/smartcar/group16/control/steering"
    private val QOS = 0
    private val REVERSE = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_option)

        val  mTraveledDistance : TextView = findViewById(R.id.distance)
        val  mFront : TextView = findViewById(R.id.front)
        val  mSpeed: TextView = findViewById(R.id.speed)

        val actionBar = supportActionBar
        actionBar!!.title = ""

        //mqtt car handler
        mqttHandler = MqttHandler(this.applicationContext, mTraveledDistance, mFront, mSpeed)
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
                val newAngle: Int = updateAngle(angle)
                val newSpeed: Int = updateSpeed(strength, angle)
                move(newSpeed, newAngle, currentSpeed, currentSpeed)
                currentAngle = newAngle
                currentSpeed = newSpeed
            }
        })
    }

    private fun move(
        newSpeed: Int,
        newAngle: Int,
        currentAngle: Int,
        currentSpeed: Int
    ) {
        var newAngle = newAngle
        if (newAngle != currentAngle || newSpeed != currentSpeed) {
            if (newSpeed == 0) newAngle = 0
            mqttHandler?.publish(STEERING_CONTROL, Integer.toString(newAngle), QOS, null)
            mqttHandler?.publish(THROTTLE_CONTROL, Integer.toString(newSpeed), QOS, null)
        }
    }

    private fun updateAngle(angle: Int): Int {
        return if (angle in 90..180) { // left
        90 - angle
    } else if (angle in 0..89) { // right
        90 - angle
    } else if (angle > 0 && angle >= 270) { // back right
        angle - 270
    } else { // back left
        angle - 270
    }
    }

    private fun updateSpeed(strength: Int, angle: Int): Int {
        val newSpeed: Double = if (angle <= 180) {
            strength * 0.8
        } else {
            strength * 0.8 * REVERSE
        }
        return newSpeed.toInt()
    }
}
