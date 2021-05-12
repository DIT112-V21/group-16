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

private Button forwardButton, backwardButton, stopButton, leftButton, rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TextView message = findViewById(R.id.message);

        setContentView(R.layout.activity_manual_option);

        //mqtt car handler
        carHandler = new CarHandler(this.getApplicationContext());
        carHandler.connectToMqttBroker();

       // setButtons();

       /* ImageButton cameraButton = findViewById(R.id.camera);
        cameraButton.setOnClickListener { v ->
            popUpWindow = PopUpWindow()
        popUpWindow.showPopupWindow(v)
        }*/

        }

       /* public void setButtons(){
        forwardButton = findViewById(R.id.forward);
        carHandler.forward(forwardButton);
        backwardButton = findViewById(R.id.backward);
        carHandler.forward(backwardButton);
        stopButton = findViewById(R.id.stopCar);
        carHandler.stop(stopButton);
        leftButton = findViewById(R.id.leftForward);
        carHandler.forwardLeft(leftButton);
        rightButton = findViewById(R.id.rightForward);
        carHandler.forwardRight(rightButton);
        }*/


}

