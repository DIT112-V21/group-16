package com.example.firstapp

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.eclipse.paho.client.mqttv3.*
import kotlin.jvm.Throws

class ManualOptionActivity : AppCompatActivity() {
    private val TAG = "app"
    private val EXTERNAL_MQTT_BROKER = "aerostun.dev"
    private val MQTT_SERVER = "tcp://$EXTERNAL_MQTT_BROKER:1883"
    private val THROTTLE_CONTROL = "/smartcar/group16/control/throttle"
    private val STEERING_CONTROL = "/smartcar/group16/control/steering"
    private val TRAVELED_DIS = "/smartcar/group16/distance"
    private val MOVEMENT_SPEED = 40
    private val IDLE_SPEED = 0
    private val STRAIGHT_ANGLE = 0
    private val STEERING_ANGLE = 10
    private val QOS = 1
    private val IMAGE_WIDTH = 320
    private val IMAGE_HEIGHT = 240

    private var mMqttClient: MqttClient? = null
    private var isConnected = false
    private var mCameraView: ImageView? = null
    private var mTraveledDistance: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_option)
        mCameraView = findViewById(R.id.imageView)
        mTraveledDistance=findViewById(R.id.textView6)
        mMqttClient = MqttClient(applicationContext, MQTT_SERVER, TAG)
        //context: Context?, serverUrl: String?, clientId: String?

        connectToMqttBroker()

        val actionBar = supportActionBar
        actionBar!!.title = ""

        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        connectToMqttBroker()
    }

    override fun onPause() {
        super.onPause()
        mMqttClient?.disconnect(object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.i(TAG, "Disconnected from broker")
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {}
        })
    }

    private fun connectToMqttBroker() {
        if (!isConnected)
        {
            mMqttClient?.connect(object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    isConnected = true

                    val successfulConnection = "Connected to MQTT broker";
                    Log.i(TAG, successfulConnection);
                    Toast.makeText(applicationContext, successfulConnection, Toast.LENGTH_SHORT)
                        ?.show()
                    mMqttClient?.subscribe("/smartcar/ultrasound/front", QOS, null);
                    mMqttClient?.subscribe("/smartcar/group16/camera", QOS, null);
                    mMqttClient?.subscribe("/smartcar/group16/distance", QOS, null);
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    val failedConnection = "Failed to connect to MQTT broker"
                    Log.e(TAG, failedConnection)
                    Toast.makeText(applicationContext, failedConnection, Toast.LENGTH_SHORT).show()
                }
            }, object : MqttCallback {
                override fun connectionLost(cause: Throwable) {
                    isConnected = false
                    val connectionLost = "Connection to MQTT broker lost"
                    Log.w(TAG, connectionLost)
                    Toast.makeText(applicationContext, connectionLost, Toast.LENGTH_SHORT).show();
                }

                @Throws(Exception::class)
                override fun messageArrived(topic: String, message: MqttMessage) {
                    if (topic == "/smartcar/group16/camera") {
                        val bm = Bitmap.createBitmap(
                                IMAGE_WIDTH,
                                IMAGE_HEIGHT,
                                Bitmap.Config.ARGB_8888
                        )

                        val payload = message.payload
                        val colors = IntArray(IMAGE_WIDTH * IMAGE_HEIGHT)
                        for (ci in colors.indices) {
                            val r = payload[3 * ci]
                            val g = payload[3 * ci + 1]
                            val b = payload[3 * ci + 2]
                            colors[ci] = Color.rgb(r.toInt(), g.toInt(), b.toInt())
                        }
                        bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
                        mCameraView?.setImageBitmap(bm)
                    }

                       if (topic == "/smartcar/group16/distance"){
                           val distance = message.payload
                           mTraveledDistance?.setText(distance.toString())
                        }
                     else {
                        Log.i(TAG, "[MQTT] Topic: $topic | Message: $message")
                    }
                }

                override fun deliveryComplete(token: IMqttDeliveryToken) {
                    Log.d(TAG, "Message delivered");
                }
            })
        }
    }

    private fun drive(throttleSpeed: Int?, steeringAngle: Int?,  actionDescription: String) {
        if (!isConnected) {
            val notConnected = "Not connected (yet)";
            Log.e(TAG, notConnected)
            Toast.makeText(applicationContext, notConnected, Toast.LENGTH_SHORT).show()
            return
        }
        Log.i(TAG, actionDescription);
        mMqttClient?.publish(THROTTLE_CONTROL, throttleSpeed.toString(), QOS, null)
        mMqttClient?.publish(STEERING_CONTROL, steeringAngle.toString(), QOS, null)

    }

    fun forward(view: View) {
        drive(MOVEMENT_SPEED, STRAIGHT_ANGLE, "Moving forward")
    }

    fun forwardLeft(view: View) {
        drive(MOVEMENT_SPEED, -STEERING_ANGLE, "Moving left")
    }

    fun forwardRight(view: View) {
        drive(MOVEMENT_SPEED, STEERING_ANGLE, "Moving right")
    }

    fun stop(view: View) {
        drive(IDLE_SPEED, STRAIGHT_ANGLE, "Stop vehicle")
    }


    fun backward(view: View) {
        drive(-MOVEMENT_SPEED, STRAIGHT_ANGLE, "Moving backwards")
    }

}

