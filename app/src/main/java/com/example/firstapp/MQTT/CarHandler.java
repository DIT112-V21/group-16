package com.example.firstapp.MQTT;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.eclipse.paho.client.mqttv3.*;

public class CarHandler extends AppCompatActivity {

    //connection to Mqtt
    private static final String TAG = "app";
    private static final String EXTERNAL_MQTT_BROKER = "aerostun.dev";
    private static final String LOCALHOST = "10.0.2.2";
    private static final String MQTT_SERVER = "tcp://" + EXTERNAL_MQTT_BROKER + ":1883";
    private static final int QOS = 1;
    private boolean isConnected = false;
    Context context;

    //Topics
    private static final String THROTTLE_CONTROL = "/smartcar/group16/control/throttle";
    private static final String STEERING_CONTROL = "/smartcar/group16/control/steering";
    private static final String CAMERA_SUB = "/smartcar/group16/camera";
    private static final String ULTRASOUND_SUB = "/smartcar/group16/ultrasound/front";

    //messages
    private static final int MOVEMENT_SPEED = 40;
    private static final int IDLE_SPEED = 0;
    private static final int STRAIGHT_ANGLE = 0;
    private static final int STEERING_ANGLE = 10;

    // Camera
    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 240;

    //messages related to connection to mqtt broker
    private static final String SUCCESSFUL_CONNECTION = "Connected to MQTT broker";
    private static final String FAILED_CONNECTION = "Failed to connect to MQTT broker";
    private static final String LOST_CONNECTION = "Connection to MQTT broker lost";
    private static final String DISCONNECTED = "Disconnected from broker";

    private ImageView cameraView;
    private MqttClient mqttClient;

    //Constructors
    public CarHandler(Context context, ImageView cameraView) {
        mqttClient = new MqttClient(context, MQTT_SERVER, TAG);
        this.cameraView = cameraView;
    }

    public CarHandler(Context context) {
        mqttClient = new MqttClient(context, MQTT_SERVER, TAG);
    }


    @Override
    protected void onResume() {
        connectToMqttBroker();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mqttClient.disconnect(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i(TAG, DISCONNECTED);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            }
        });
    }

    public void connectToMqttBroker() {
        if (!isConnected) {
            mqttClient.connect(TAG, "", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;
                    Log.i(TAG, SUCCESSFUL_CONNECTION);
                    // Toast.makeText(getApplicationContext(), successfulConnection, Toast.LENGTH_SHORT).show();
                    //message.setText(SUCCESSFUL_CONNECTION);
                    message(SUCCESSFUL_CONNECTION);

                    // mqttClient.subscribe(ULTRASOUND_SUB, QOS, null);
                    mqttClient.subscribe(CAMERA_SUB, QOS, null);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, FAILED_CONNECTION);
                    //Toast.makeText(getApplicationContext(), failedConnection, Toast.LENGTH_SHORT).show();
                   // message.setText(FAILED_CONNECTION);
                    message(FAILED_CONNECTION);
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    isConnected = false;
                    Log.w(TAG, LOST_CONNECTION);
                    //Toast.makeText(getApplicationContext(), connectionLost, Toast.LENGTH_SHORT).show();
                   // message.setText(LOST_CONNECTION);
                    message(LOST_CONNECTION);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (topic.equals(CAMERA_SUB)) {
                        setUpCamera(message);
                    } else {
                        Log.i(TAG, "[MQTT] Topic: " + topic + " | Message: " + message.toString());
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Message delivered");
                }
            });
        }
    }

    public void start(){
        connectToMqttBroker();
    }

    public void setUpCamera(MqttMessage message) {
        final Bitmap bm = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);

        final byte[] payload = message.getPayload();
        final int[] colors = new int[IMAGE_WIDTH * IMAGE_HEIGHT];
        for (int ci = 0; ci < colors.length; ++ci) {
            final byte r = payload[3 * ci];
            final byte g = payload[3 * ci + 1];
            final byte b = payload[3 * ci + 2];
            colors[ci] = Color.rgb(r, g, b);
        }
        bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        cameraView.setImageBitmap(bm);
    }

    public void message(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    public void publish(String topic, String message, int qos, IMqttActionListener publishCallback) {
        mqttClient.publish(topic, message, qos, publishCallback);
    }

    public void disconnect(IMqttActionListener disconnectionCallback) {
        mqttClient.disconnect(disconnectionCallback);
    }

    public void subscribe(String topic, int qos, IMqttActionListener subscriptionCallback) {
        mqttClient.subscribe(topic, qos, subscriptionCallback);
    }

    public void unsubscribe(String topic, IMqttActionListener unsubscriptionCallback) {
        mqttClient.unsubscribe(topic, unsubscriptionCallback);
    }

    void drive(int throttleSpeed, int steeringAngle, String actionDescription) {
        if (!isConnected) {
            final String notConnected = "Not connected (yet)";
            Log.e(TAG, notConnected);
            Toast.makeText(getApplicationContext(), notConnected, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, actionDescription);
        mqttClient.publish(THROTTLE_CONTROL, Integer.toString(throttleSpeed), QOS, null);
        mqttClient.publish(STEERING_CONTROL, Integer.toString(steeringAngle), QOS, null);
    }

    public void forward(View view) {
        drive(MOVEMENT_SPEED, STRAIGHT_ANGLE, "Moving forward");
    }

    public void forwardLeft(View view) {
        drive(MOVEMENT_SPEED, -STEERING_ANGLE, "Moving forward left");
    }

    public void stop(View view) {
        drive(IDLE_SPEED, STRAIGHT_ANGLE, "Stopping");
    }

    public void forwardRight(View view) {
        drive(MOVEMENT_SPEED, STEERING_ANGLE, "Moving forward left");
    }

    public void backward(View view) {
        drive(-MOVEMENT_SPEED, STRAIGHT_ANGLE, "Moving backward");
    }

}

