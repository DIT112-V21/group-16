package com.example.firstapp.mqtt
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.RED
import android.graphics.Color.BLACK
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.eclipse.paho.client.mqttv3.*

class MqttHandler : AppCompatActivity {

    //Connection to Mqtt
    private var mMqttClient: MqttClient? = null
    private val TAG = "app"
    private val EXTERNAL_MQTT_BROKER = "aerostun.dev"
    private val LOCALHOST = "10.0.2.2"
    private val MQTT_SERVER = "tcp://" + LOCALHOST + ":1883"
    private val QOS = 1
    private var isConnected = false
    private var context: Context? = null

    //Subscription topics
    private val CAMERA_SUB = "/smartcar/group16/camera"
    private val ULTRASOUND_SUB = "/smartcar/group16/obstacleMsg"
    private val TRAVELED_DIS = "/smartcar/group16/distance"
    private val BIN_CAPACITY = "/smartcar/group16/bagfull"

    // Publishing topics
    private val THROTTLE_CONTROL = "/smartcar/group16/control/throttle"
    private val STEERING_CONTROL = "/smartcar/group16/control/steering"
    private val AUTO_SPEED = "/smartcar/group16/auto/speed"
    private val AUTO_PATTERN = "/smartcar/group16/auto/pattern"
    private val AUTO_SIZE = "/smartcar/group16/auto/size"

    // Camera view sizing
    private val IMAGE_WIDTH = 320
    private val IMAGE_HEIGHT = 240

    //Messages related to connection to mqtt broker
    private val SUCCESSFUL_CONNECTION = "Connected to MQTT broker"
    private val FAILED_CONNECTION = "Failed to connect to MQTT broker"
    private val LOST_CONNECTION = "Connection to MQTT broker lost"
    private val DISCONNECTED = "Disconnected from broker"

    private var mCameraView: ImageView? = null
    private var mTraveledDistance: TextView? = null
    private var mFront: TextView? = null
    private var mBagCapacity: TextView? = null

    //Constructors
    constructor(context: Context?, mCameraView: ImageView?) {
        mMqttClient = MqttClient(context, MQTT_SERVER, TAG)
        this.mCameraView = mCameraView
        this.context = context
    }

    constructor(context: Context?, mTraveledDistance: TextView?, mFront: TextView?) {
        mMqttClient = MqttClient(context, MQTT_SERVER, TAG)
        this.context = context
        this.mTraveledDistance = mTraveledDistance
        this.mFront = mFront
    }

    constructor(context: Context?, mBagCapacity: TextView?) {
        mMqttClient = MqttClient(context, MQTT_SERVER, TAG)
        this.context = context
        this.mBagCapacity = mBagCapacity
    }

    override fun onResume() {
        connectToMqttBroker()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMqttClient?.disconnect(object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.i(TAG, DISCONNECTED)
            }
            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {}
        })
    }

    fun connectToMqttBroker() {
        if (!isConnected) {
            mMqttClient?.connect(TAG, "", object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    isConnected = true
                    Log.i(TAG, SUCCESSFUL_CONNECTION)
                    subscriptions()
                }
                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.e(TAG, FAILED_CONNECTION)
                }
            }, object : MqttCallback {
                override fun connectionLost(cause: Throwable) {
                    isConnected = false
                    Log.w(TAG, LOST_CONNECTION)
                }
                @SuppressLint("SetTextI18n")
                @Throws(Exception::class)
                override fun messageArrived(topic: String, message: MqttMessage) {
                    if (topic == CAMERA_SUB) {
                        setCameraView(message)
                    }
                    if (topic == TRAVELED_DIS) {
                        setDistanceView(message)
                    }
                    if (topic == ULTRASOUND_SUB) {
                        setWarningView(message)
                    }
                    if (topic == BIN_CAPACITY) {
                        setBinView(message)
                    } else {
                        Log.i(
                            TAG,
                            "[MQTT] Topic: $topic | Message: $message"
                        )
                    }
                }
                override fun deliveryComplete(token: IMqttDeliveryToken) {
                    Log.d(TAG, "Message delivered")
                }
            })
        }
    }

    fun notConnected() {
        if (!isConnected) {
            val notConnected = "Not connected (yet)"
            Log.e(TAG, notConnected)
            Toast.makeText(context!!.applicationContext, notConnected, Toast.LENGTH_SHORT).show()
            return
        }
    }

    fun setCameraView(message : MqttMessage){
        val bm =
            Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888)
        val payload = message.payload
        val colors = IntArray(IMAGE_WIDTH * IMAGE_HEIGHT)
        for (ci in colors.indices) {
            val r = payload[3 * ci]
            val g = payload[3 * ci + 1]
            val b = payload[3 * ci + 2]
            colors[ci] = Color.rgb(r.toInt(), g.toInt(), b.toInt())
        }
        bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT)
        mCameraView?.setImageBitmap(bm)
    }

    @SuppressLint("SetTextI18n")
    fun setDistanceView(message : MqttMessage) {
        val distance = message.toString()
        mTraveledDistance?.text = "$distance m"
    }

    fun setWarningView(message: MqttMessage){
        val ultraSound = message.toString()
        if (ultraSound == "obstacle") {
            mFront?.text = "WARNING"
            mFront?.setTextColor(RED)
        } else {
            mFront?.text = "No Obstacle"
            mFront?.setTextColor(BLACK)
        }
    }

    fun setBinView(message : MqttMessage){
        val capacity = message.toString()
        mBagCapacity?.text = "${capacity}% "
    }

    fun subscriptions(){
        mMqttClient?.subscribe(ULTRASOUND_SUB, QOS, null)
        mMqttClient?.subscribe(CAMERA_SUB, QOS, null)
        mMqttClient?.subscribe(TRAVELED_DIS, QOS, null)
        mMqttClient?.subscribe(BIN_CAPACITY, QOS, null)
    }
    fun drive(throttleSpeed: Int, steeringAngle: Int, actionDescription: String?) {
        notConnected()
        Log.i(TAG, actionDescription!!)
        mMqttClient?.publish(THROTTLE_CONTROL, throttleSpeed.toString(), QOS, null)
        mMqttClient?.publish(STEERING_CONTROL, steeringAngle.toString(), QOS, null)
    }

    fun driveAuto(size : Int, speed : Int, pattern: Int, actionDescription: String?) {
        notConnected()
        Log.i(TAG, actionDescription!!)
        mMqttClient?.publish(AUTO_SIZE, size.toString(), QOS, null)
        mMqttClient?.publish(AUTO_SPEED, speed.toString(), QOS, null)
        mMqttClient?.publish(AUTO_PATTERN, pattern.toString(), QOS, null)
    }
}