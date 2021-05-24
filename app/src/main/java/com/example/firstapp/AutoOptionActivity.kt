package com.example.firstapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
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
    private var mEmptyBtn: Button? = null

   //progressbar variable
    private var isStarted = false
    private var progressStatus = 0
    private var handler: Handler? = null
    private var mBagfull=0

    //Messages
    private val PATTERN_ONE = 1
    private val PATTERN_TWO = 2
    private var mSpeed = 0
    private var mPattern = 0
    private var mSize = 0

    // seekbar pointers
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
       // mCameraButton = findViewById(R.id.cameraBtn)
        mStartBtn = findViewById(R.id.start_cleaning)
        mEmptyBtn= findViewById(R.id.empty)

        mPatternOne?.setOnClickListener(this)
        mPatternTwo?.setOnClickListener(this)
        mStartBtn?.setOnClickListener(this)
        mEmptyBtn?.setOnClickListener(this)
        mCameraButton?.setOnClickListener(this)

        val actionBar = supportActionBar
        actionBar!!.title = ""

        //mqtt car handler
        mqttHandler = MqttHandler(this.applicationContext, isStarted)
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

        // Cleaning start bagfull progressBar 2
        handler = Handler(Handler.Callback {
            var aProgressBar: ProgressBar = findViewById<ProgressBar>(R.id.progressBar3)
            if (isStarted && progressStatus < 100) {
                progressStatus++
                if (progressStatus == 100) {
                    Toast.makeText(
                        applicationContext,
                        "Waste Bag is full, please empty bag",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            aProgressBar.progress = progressStatus
            var progressView = findViewById<TextView>(R.id.textViewProgress)
            // progressView.text = "${progressStatus}% "
            handler?.sendEmptyMessageDelayed(0, 4000)
            true
        })
        handler?.sendEmptyMessage(0)
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
                    sendMessages(mSpeed, mPattern, mSize)
                }
                R.id.cameraBtn ->{
                    //openCameraWindow()
                }
                R.id.empty->{
                    progressStatus = 0
                    isStarted = false

                }
        }
    }

    private fun getSizeInput(){
        mSizeField = findViewById(R.id.size_input);
        var temp: String = mSizeField?.text.toString()
        var value: Int
        if ("" != temp) {
            value = Integer.parseInt(temp)
            mSize = value
        }
    }

  private fun sendMessages(speed : Int, pattern : Int, size : Int) {
      if (speed != 0 && pattern != 0 && size != 0 ){
       mqttHandler!!.driveAuto(speed, pattern, size, "")
      }
  }
}


