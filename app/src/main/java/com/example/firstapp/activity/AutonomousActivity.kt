package com.example.firstapp.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.mqtt.MqttHandler
import com.example.firstapp.R


class AutonomousActivity : AppCompatActivity(), View.OnClickListener {

    private var mqttHandler: MqttHandler? = null
    private var mPatternOne: Button? = null
    private var mPatternTwo: Button? = null
    private var mStartBtn: Button? = null
    private var mSizeField: EditText? = null
    private var mSeekBar: SeekBar? = null
    private var mSpeedText : TextView? = null

    //Messages
    private val PATTERN_ONE = 1
    private val PATTERN_TWO = 2
    private var mSpeed = 0
    private var mPattern = 0
    private var mSize = 0

    //Seekbar pointers
    private var mStartPoint = 0
    private var mEndPoint = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_option)

        mPatternOne = findViewById(R.id.pattern1)
        mPatternTwo = findViewById(R.id.pattern2)
        mSpeedText = findViewById(R.id.velocity)
        mSeekBar = findViewById(R.id.seekBar)
        mStartBtn = findViewById(R.id.start_cleaning)

        mPatternOne?.setOnClickListener(this)
        mPatternTwo?.setOnClickListener(this)
        mStartBtn?.setOnClickListener(this)

        val actionBar = supportActionBar
        actionBar!!.title = ""

        val  mBagCapacity : TextView = findViewById(R.id.binProgress)

        //Mqtt car handler
        mqttHandler = MqttHandler(this.applicationContext, mBagCapacity)
        mqttHandler!!.connectToMqttBroker()

        //Seekbar to get input from user regarding velocity
        mSeekBar?.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seek: SeekBar,
                                               progress: Int, fromUser: Boolean) {
                    mSpeedText?.text = progress.toString()
                    mSpeed = Integer.parseInt(mSpeedText?.text as String)
                }
                override fun onStartTrackingTouch(seek: SeekBar) {
                   mStartPoint = mSeekBar!!.progress
                }
                override fun onStopTrackingTouch(seek: SeekBar) {
                   mEndPoint = mSeekBar!!.progress
                }
            })
    }

    override fun onClick(v: View?) {
            when (v?.id) {
                R.id.pattern1 -> {
                    v.setBackgroundColor(Color.LTGRAY)
                    mPattern = PATTERN_ONE
                    mPatternTwo?.setBackgroundColor(Color.WHITE)
                }
                R.id.pattern2 ->{
                    v.setBackgroundColor(Color.LTGRAY)
                    mPattern = PATTERN_TWO
                    mPatternOne?.setBackgroundColor(Color.WHITE)
                }
                R.id.start_cleaning ->{
                    getSizeInput()
                    sendMessages(mSize, mSpeed, mPattern)
                }
             }
        }

    private fun getSizeInput(){
        mSizeField = findViewById(R.id.size_input);
        val temp: String = mSizeField?.text.toString()
        val value: Int
        if ("" != temp) {
            value = Integer.parseInt(temp)
            mSize = value
        }
    }

  private fun sendMessages(size : Int, speed : Int, pattern : Int) {
      if (size != 0 && speed != 0 && pattern != 0 ){
       mqttHandler!!.driveAuto(size, speed, pattern,"")
      }
  }
}