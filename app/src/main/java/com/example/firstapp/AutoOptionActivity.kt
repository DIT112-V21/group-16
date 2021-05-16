package com.example.firstapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.MQTT.MqttHandler


class AutoOptionActivity : AppCompatActivity() {

    private var mqttHandler: MqttHandler? = null
    private var mCameraButton: ImageButton? = null
    private var mPatternOne: Button? = null
    private var mPatternTwo: Button? = null
    private var mLowBtn: Button? = null
    private var mBig : Button? = null
    private var mMedium : Button? = null
    private var mSmall : Button? = null
    private var mMediumBtn: Button? = null
    private var mHighBtn: Button? = null
    private var mStart : Button? = null
    //private var mSize : EditText? = null

    //Messages
    private val BIG_SIZE = 6400
    private val MEDIUM_SIZE = 3600
    private val SMALL_SIZE = 1600
    private val LOW_SPEED = 20
    private val HIGH_SPEED = 60
    private val MEDIUM_SPEED = 40
    private val PATTERN_ONE = 1
    private val PATTERN_TWO = 2
    private val START_CLEANING = "START"

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

            imageView.setOnClickListener {
                window.dismiss()
            }
            window.showAsDropDown(mCameraButton)
        }

        mSmall = findViewById(R.id.small)
        mSmall?.setOnClickListener {
            mqttHandler!!.sendSize(SMALL_SIZE, "")
        }
        mMedium = findViewById(R.id.medium2)
        mMedium?.setOnClickListener {
            mqttHandler!!.sendSize(MEDIUM_SIZE, "")
        }
        mBig = findViewById(R.id.big)
        mBig?.setOnClickListener {
            mqttHandler!!.sendSize(BIG_SIZE, "")
        }

        mStart = findViewById(R.id.start_cleaning)
        mStart?.setOnClickListener {
            mqttHandler!!.startCleaning(START_CLEANING, "")
        }

        mLowBtn = findViewById(R.id.low)
        mLowBtn?.setOnClickListener {
            mqttHandler!!.sendSpeed(LOW_SPEED, "")
        }
        mMediumBtn = findViewById(R.id.medium)
        mMediumBtn?.setOnClickListener {
            mqttHandler!!.sendSpeed(MEDIUM_SPEED, "")
        }
        mHighBtn = findViewById(R.id.high)
        mHighBtn?.setOnClickListener {
            mqttHandler!!.sendSpeed(HIGH_SPEED, "")
        }
        mPatternOne = findViewById(R.id.pattern1)
        mPatternOne?.setOnClickListener {
            mqttHandler!!.sendPattern(PATTERN_ONE, "")
        }
        mPatternTwo = findViewById(R.id.pattern2)
        mPatternTwo?.setOnClickListener {
            mqttHandler!!.sendPattern(PATTERN_TWO, "")
        }
    }
}
