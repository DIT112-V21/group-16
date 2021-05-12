package com.example.firstapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstapp.MQTT.MqttClient;

import androidx.appcompat.app.AppCompatActivity;
import com.example.firstapp.MQTT.CarHandler;


public class ManualOptionActivity extends AppCompatActivity {

    private CarHandler carHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView message = findViewById(R.id.message);
        setContentView(R.layout.activity_manual_option);

        //mqtt car handler
        carHandler = new CarHandler(this.getApplicationContext());
        carHandler.connectToMqttBroker(message);

       /* ImageButton cameraButton = findViewById(R.id.camera);
        cameraButton.setOnClickListener { v ->
            popUpWindow = PopUpWindow()
        popUpWindow.showPopupWindow(v)
        }*/

        }
}

