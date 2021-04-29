package com.example.firstapp

import android.content.Intent
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.databinding.ActivityMainBinding
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException


class MainActivity : AppCompatActivity() {
   private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
        val actionBar = supportActionBar
        actionBar!!.title = ""

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startCleaning.setOnClickListener {
            binding.startCleaning.text = "start"
            startActivity(Intent(this, CleaningOptionActivity::class.java))
        }
           /* val clientId = MqttClient.generateClientId()
            val client = MqttAndroidClient(
                this.applicationContext, "tcp://aerostun.dev:1883",
                clientId
            )

            try {
                val token = client.connect()
                token.actionCallback = object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken) {
                        // We are connected
                        Log.d(TAG, "onSuccess")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                        // Something went wrong e.g. connection timeout or firewall problems
                        Log.d(TAG, "onFailure")
                    }
                }
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        }*/


    }
}