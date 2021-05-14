package com.example.firstapp.MQTT

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.eclipse.paho.client.mqttv3.*


class MqttHandler : AppCompatActivity {

    //connection to Mqtt
    private val TAG = "app"
    private val EXTERNAL_MQTT_BROKER = "aerostun.dev"
    private val LOCALHOST = "10.0.2.2"
    private val MQTT_SERVER = "tcp://" + EXTERNAL_MQTT_BROKER + ":1883"
    private val QOS = 1
    private var isConnected = false
    private var context: Context? = null

    //Topics
    private val THROTTLE_CONTROL = "/smartcar/group16/control/throttle"
    private val STEERING_CONTROL = "/smartcar/group16/control/steering"
    private val CAMERA_SUB = "/smartcar/group16/camera"
    private val ULTRASOUND_SUB = "/smartcar/group16/ultrasound/front"
    private val TRAVELED_DIS = "/smartcar/group16/distance"
    private val SPEED_SUB = "/smartcar/group16/speed"
    private val BAG_FULL="/smartcar/smartcar/group16/bagfull"

    //messages
    private val MOVEMENT_SPEED = 40
    private val IDLE_SPEED = 0
    private val STRAIGHT_ANGLE = 0
    private val STEERING_ANGLE = 10

    // Camera
    private val IMAGE_WIDTH = 320
    private val IMAGE_HEIGHT = 240

    //messages related to connection to mqtt broker
    private val SUCCESSFUL_CONNECTION = "Connected to MQTT broker"
    private val FAILED_CONNECTION = "Failed to connect to MQTT broker"
    private val LOST_CONNECTION = "Connection to MQTT broker lost"
    private val DISCONNECTED = "Disconnected from broker"

    private var mCameraView: ImageView? = null
    private var mMqttClient: MqttClient? = null
    private var mTraveledDistance: TextView? = null
    private var mFront: TextView? = null
    private var mSpeed: TextView? = null
    private var mBagfull: TextView?=null

    //Constructors
    constructor(context: Context?, mCameraView: ImageView?) {
        mMqttClient = MqttClient(context, MQTT_SERVER, TAG)
        this.mCameraView = mCameraView
        this.context = context
    }

    constructor(context: Context?, mTraveledDistance : TextView?, mSpeed : TextView?, mFront : TextView?) {
        mMqttClient = MqttClient(context, MQTT_SERVER, TAG)
        this.context = context
        this.mTraveledDistance = mTraveledDistance
        this.mFront = mFront
        this.mSpeed = mSpeed

    }
    //UI update required from text view to progressbar
    constructor(context:Context?,mBagfull: TextView?){
        this.mBagfull=mBagfull

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
                    // Toast.makeText(getApplicationContext(), successfulConnection, Toast.LENGTH_SHORT).show();
                    //message.text = SUCCESSFUL_CONNECTION
                    message(SUCCESSFUL_CONNECTION)
                    mMqttClient?.subscribe(ULTRASOUND_SUB, QOS, null)
                    mMqttClient?.subscribe(CAMERA_SUB, QOS, null)
                    mMqttClient?.subscribe(TRAVELED_DIS, QOS, null)
                    mMqttClient?.subscribe(SPEED_SUB, QOS, null)
                    mMqttClient?.subscribe(BAG_FULL, QOS, null)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.e(TAG, FAILED_CONNECTION)
                    //Toast.makeText(getApplicationContext(), failedConnection, Toast.LENGTH_SHORT).show();
                    //message.text = FAILED_CONNECTION
                    message(FAILED_CONNECTION)
                }
            }, object : MqttCallback {
                override fun connectionLost(cause: Throwable) {
                    isConnected = false
                    Log.w(TAG, LOST_CONNECTION)
                    //Toast.makeText(getApplicationContext(), connectionLost, Toast.LENGTH_SHORT).show();
                    //message.text = LOST_CONNECTION
                    message(LOST_CONNECTION)
                }

                @Throws(Exception::class)
                override fun messageArrived(topic: String, message: MqttMessage) {
                    if (topic == CAMERA_SUB) {
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
                    if (topic == TRAVELED_DIS) {
                        val distance = message.toString()
                        mTraveledDistance?.setText(distance + " cm")
                    }
                        if (topic == ULTRASOUND_SUB) {
                            val ultraSound = message.toString()
                            mFront?.setText(ultraSound + " cm")
                        }
                    if (topic == SPEED_SUB) {
                        val speed = message.toString()
                        mSpeed?.setText(speed)}
                    if (topic == BAG_FULL) {
                        val BagStatus = message.toString()
                        mBagfull?.setText(BagStatus+"%")
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

    fun message(message: String?) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    fun publish(topic: String?, message: String?, qos: Int, publishCallback: IMqttActionListener?) {
        if (message != null) {
            mMqttClient?.publish(topic, message, qos, publishCallback)
        }
    }

    fun disconnect(disconnectionCallback: IMqttActionListener?) {
        mMqttClient?.disconnect(disconnectionCallback)
    }

    fun subscribe(topic: String?, qos: Int, subscriptionCallback: IMqttActionListener?) {
        mMqttClient?.subscribe(topic, qos, subscriptionCallback)
    }

    fun unsubscribe(topic: String?, unsubscriptionCallback: IMqttActionListener?) {
        mMqttClient?.unsubscribe(topic, unsubscriptionCallback)
    }

    fun drive(throttleSpeed: Int, steeringAngle: Int, actionDescription: String?) {
        if (!isConnected) {
            val notConnected = "Not connected (yet)"
            Log.e(TAG, notConnected)
            Toast.makeText(context!!.applicationContext, notConnected, Toast.LENGTH_SHORT).show()
            return
        }
        Log.i(TAG, actionDescription!!)
        mMqttClient?.publish(THROTTLE_CONTROL, Integer.toString(throttleSpeed), QOS, null)
        mMqttClient?.publish(STEERING_CONTROL, Integer.toString(steeringAngle), QOS, null)
    }

   /* fun forward(view: View?) {
        drive(MOVEMENT_SPEED, STRAIGHT_ANGLE, "Moving forward")
    }

    fun forwardLeft(view: View?) {
        drive(MOVEMENT_SPEED, -STEERING_ANGLE, "Moving forward left")
    }

    fun forwardRight(view: View?) {
        drive(MOVEMENT_SPEED, STEERING_ANGLE, "Moving forward left")
    }

    fun backward(view: View?) {
        drive(-MOVEMENT_SPEED, STRAIGHT_ANGLE, "Moving backward")
    }*/

    fun stop(view: View?) {
        drive(IDLE_SPEED, STRAIGHT_ANGLE, "Stopping")
    }

}


