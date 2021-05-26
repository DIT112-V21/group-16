package com.example.firstapp.activity
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.R
import com.example.firstapp.MQTT.MqttHandler
import io.github.controlwear.virtual.joystick.android.JoystickView
import io.github.controlwear.virtual.joystick.android.JoystickView.OnMoveListener

class ManualCleaningActivity : AppCompatActivity() {

    private var mMqttHandler: MqttHandler? = null
    private var mCameraButton : ImageButton? = null
    private val REVERSE = -1

    //progressbar attributes
    private var isStarted = false
    private var progressStatus = 0
    private var handler: Handler? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_option)

        val actionBar = supportActionBar
        actionBar!!.title = ""

        val  mTraveledDistance : TextView = findViewById(R.id.distance)
        val  mSpeed: TextView = findViewById(R.id.speed)
        val  mFront : TextView = findViewById(R.id.front)
        val mStartBtn: Button = findViewById(R.id.start)
        val mEmptyBtn: Button = findViewById(R.id.empty_b)

        //mqtt car handler
         mMqttHandler = MqttHandler(this.applicationContext, mTraveledDistance, mSpeed, mFront)
         mMqttHandler!!.connectToMqttBroker()

        // Transition to the popup window when clicking on the camera button
        mCameraButton = findViewById(R.id.camera)
        mCameraButton?.setOnClickListener {
            displayCameraView()
        }
        // Cleaning start bagfull progressBar
        handler = Handler(Handler.Callback {
            var mProgressBar: ProgressBar = findViewById<ProgressBar>(R.id.progressBar)
            if (isStarted && progressStatus <100) {
                progressStatus++
                if(progressStatus==100){
                    Toast.makeText(applicationContext, "Waste Bag is full, please empty bag", Toast.LENGTH_LONG).show()
                }
            }
            mProgressBar.progress = progressStatus
            var progressView=findViewById<TextView>(R.id.textView10)
            progressView.text = "${progressStatus}% "
            handler?.sendEmptyMessageDelayed(0, 4000)
            true
        })
        handler?.sendEmptyMessage(0)
        mStartBtn.setOnClickListener {
            isStarted = ! isStarted
        }
        //empty the bag
        mEmptyBtn.setOnClickListener {
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
                    newSpeed = driveForwards(strength)
                } else if (angle in 0..89) {
                    newAngle = turnForwards(angle)
                    newSpeed = driveForwards(strength)
                } else if (angle > 0 && angle >= 270) {
                    newAngle = turnBackwards(angle)
                    newSpeed = driveBackwards(strength)
                } else {
                    newAngle = turnBackwards(angle)
                    newSpeed = driveBackwards(strength)
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

    private fun displayCameraView() {
        val window = PopupWindow(this.applicationContext)
        val view = layoutInflater.inflate(R.layout.pop_up_window, null)
        window.contentView = view
        val imageView = view.findViewById<ImageView>(R.id.camera)

        mMqttHandler = MqttHandler(this.applicationContext, imageView)
        mMqttHandler!!.connectToMqttBroker()

        imageView.setOnClickListener {
            window.dismiss()
        }
        window.showAsDropDown(mCameraButton)
    }

    private fun driveForwards(strength: Int) : Int{
        val strength = (strength * 0.8).toInt()
        return strength
    }

    private fun driveBackwards(strength : Int) : Int{
        val strength = (strength * 0.5 * REVERSE).toInt()
        return strength
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
        mMqttHandler?.drive(newSpeed, newAngle, "")
    }
}