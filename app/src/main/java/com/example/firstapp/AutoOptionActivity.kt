package com.example.firstapp

import android.R.attr.angle
import android.graphics.Color
import android.icu.text.CaseMap
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.MQTT.MqttHandler


class AutoOptionActivity : AppCompatActivity(), View.OnClickListener {

    private var mqttHandler: MqttHandler? = null
    private var mCameraButton: ImageButton? = null
    private var mPatternOne: Button? = null
    private var mPatternTwo: Button? = null
    private var mStartBtn: Button? = null
    private var mSizeField: EditText? = null
    private var mSeekBar: SeekBar? = null
    private var mSpeedText : TextView? = null

    //Messages
    private val PATTERN_ONE = 1
    private val PATTERN_TWO = 2
    private val START_CLEANING = "start"
    private val STOP = "stop"

    private var speed = 0
    private var pattern = 0
    private var size = 0
    private var startPoint = 0
    private var endPoint = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_option)

        mPatternOne = findViewById(R.id.pattern1)
        mPatternTwo = findViewById(R.id.pattern2)
        mSpeedText = findViewById(R.id.velocity)
        mSeekBar = findViewById(R.id.seekBar)
        mCameraButton = findViewById(R.id.camera)
        mStartBtn = findViewById(R.id.start_cleaning)

        mPatternOne?.setOnClickListener(this)
        mPatternTwo?.setOnClickListener(this)

        val actionBar = supportActionBar
        actionBar!!.title = ""

        //mqtt car handler
        mqttHandler = MqttHandler(this.applicationContext)
        mqttHandler!!.connectToMqttBroker()

        mSeekBar?.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seek: SeekBar,
                                               progress: Int, fromUser: Boolean) {
                    mSpeedText?.text = progress.toString()
                    speed = Integer.parseInt(mSpeedText?.text as String)
                }
                override fun onStartTrackingTouch(seek: SeekBar) {
                   startPoint = mSeekBar!!.progress
                }
                override fun onStopTrackingTouch(seek: SeekBar) {
                   endPoint = mSeekBar!!.progress
                }
            })

        // Transition to the popup window when clicking on the camera button
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

        mStartBtn?.setOnClickListener {
            mSizeField = findViewById(R.id.size_input);
            var temp: String = mSizeField?.text.toString()
            var value: Int
            if ("" != temp) {
                value = Integer.parseInt(temp)
                size = value
            }
            connect(speed, pattern, size, START_CLEANING)
        }
    }

    override fun onClick(v: View?) {
            when (v?.id) {
                R.id.pattern1 -> {
                    v.setBackgroundColor(Color.LTGRAY)
                    pattern = PATTERN_ONE
                    mPatternTwo?.setBackgroundColor(Color.WHITE)
                }
                R.id.pattern2 ->{
                    v.setBackgroundColor(Color.LTGRAY)
                    pattern = PATTERN_TWO
                    mPatternOne?.setBackgroundColor(Color.WHITE)
                }
                R.id.stop ->{
                connect(0,0,0,STOP)
                }
        }
    }

  private fun connect(speed : Int, pattern : Int, size : Int, command : String) {
      if (speed != 0 && pattern != 0 && command != ""){
          mqttHandler!!.driveAuto(speed, pattern, size, command, "")
      }
  }
}


