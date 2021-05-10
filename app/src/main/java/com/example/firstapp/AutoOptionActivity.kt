package com.example.firstapp


import android.app.Activity
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class AutoOptionActivity : AppCompatActivity() {

    private val EXTERNAL_MQTT_BROKER = "aerostun.dev"
    private val MQTT_SERVER = "tcp://$EXTERNAL_MQTT_BROKER:1883"
    private val TAG = "app"
    private val Travled_dis = "/smartcar/group16/control/distance"
    private val QOS = 1
    private var mMqttClient: MqttClient? = null
    private var isConnected = false
    private var traveledDistce: TextureView? = null  //Subsribe from MQTT
    private lateinit var binding: AutoOptionActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_option)


        Thread(Runnable {
            var  bagfilled=0
            while(bagfilled<=100){
                bagfilled+=10
               // progressBar=setProgress(bagfilled)
                Thread.sleep(3000)

        }

    })

    }
   //progress bar is activated when start button(car start) is pressed..
   class MyActivity : Activity() {
       override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
           setContentView(R.layout.activity_auto_option)
           val button: Button = findViewById(R.id.button)
           button.setOnClickListener(View.OnClickListener {
               // Code here executes on main thread after user presses button
           })
       }
   }
}