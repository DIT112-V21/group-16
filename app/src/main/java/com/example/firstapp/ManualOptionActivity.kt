package com.example.firstapp
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat.postDelayed
import com.example.firstapp.MQTT.MqttHandler
import io.github.controlwear.virtual.joystick.android.JoystickView
import io.github.controlwear.virtual.joystick.android.JoystickView.OnMoveListener



class ManualOptionActivity : AppCompatActivity() {

    private var mqttHandler: MqttHandler? = null
    private var mCameraButton: ImageButton? = null
    private val THROTTLE_CONTROL = "/smartcar/group16/control/throttle"
    private val STEERING_CONTROL = "/smartcar/group16/control/steering"
    private val QOS = 0
    private val REVERSE = -1

    //progress Bar attribute
    //private var mPbText: TextView? = null
    //
    var isStarted = false
    var progressStatus = 0
    var handler: Handler? = null




    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_option)
        var progressBar: ProgressBar = findViewById(R.id.progressBar)
        val btn1: Button = findViewById(R.id.cleaning_start)
        val btn2: Button = findViewById(R.id.empty_b)
        val mTraveledDistance: TextView = findViewById(R.id.distance)
        val mSpeed: TextView = findViewById(R.id.speed)
        val mFront: TextView = findViewById(R.id.front)
       // val toggle: ToggleButton = findViewById(R.id.start)

        //val mPbText: TextView = findViewById(R.id.bagfull)

        val actionBar = supportActionBar
        actionBar!!.title = ""
        //var handler = Handler()


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

            imageView.setOnClickListener {
                window.dismiss()
            }
            window.showAsDropDown(mCameraButton)
        }

        handler = Handler(Handler.Callback {
            var progressBar: ProgressBar = findViewById<ProgressBar>(R.id.progressBar)
            if (isStarted && progressStatus <100) {
                progressStatus++
                if(progressStatus==100){
                    Toast.makeText(applicationContext, "Waste Bag is full, please empty bag", Toast.LENGTH_LONG).show()

                }
            }
            progressBar.progress = progressStatus
            var progressView=findViewById<TextView>(R.id.textViewProgress)
            progressView.text = "Bag full ${progressStatus}% "
            handler?.sendEmptyMessageDelayed(0, 100)

            true
        })

        handler?.sendEmptyMessage(0)


        btn1.setOnClickListener {
            isStarted = ! isStarted

        }
        //empty the bag
        btn2.setOnClickListener {
            progressStatus = 0
            isStarted = false
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

    private fun turnForwards(angle: Int): Int {
        val angle = 90 - angle
        return angle
    }

    private fun turnBackwards(angle: Int): Int {
        val angle = angle - 270
        return angle
    }

    private fun sendMovement(newSpeed: Int, newAngle: Int) {
        mqttHandler?.drive(newSpeed, newAngle, "")
    }

}









