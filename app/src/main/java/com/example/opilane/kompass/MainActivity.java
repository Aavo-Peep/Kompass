package com.example.opilane.kompass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private ImageView kompassPilt;
    TextView suund;
    TextView loe;
    boolean activityRunning;
    //salvestame kompassipildi pöördenurka
    private float algusedKraadid = 0f;
    // seadme sensor manager
    private SensorManager sensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // lähtestame muutujad
        kompassPilt = findViewById(R.id.kompassPio);
        suund = findViewById(R.id.nurk);
        loe = (TextView) findViewById(R.id.loe);
        // android seadme andurite funktsioonide lähtestamine
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        activityRunning = true;
        Sensor loeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(loeSensor != null) {
            sensorManager.registerListener(this, loeSensor, sensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Lugeja pole saadaval!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // saame nurga/kraadid z-teljelt kui seda pööratud
        float kraadinurk = Math.round(event.values[0]);
        // kuvame oma textviews toimunud kraadi muudatuse
        suund.setText("Suund: " + Float.toString(kraadinurk) + " kraadi");
        // loome pöörlemis animatsiooni
        RotateAnimation pöörlemisAnimatsioon = new RotateAnimation(algusedKraadid, -kraadinurk,
                Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        // defineerime animatsiooni kestuvse
        pöörlemisAnimatsioon.setDuration(200);
        pöörlemisAnimatsioon.setFillAfter(true);
        kompassPilt.startAnimation(pöörlemisAnimatsioon);
        // salvestame uued kraadid algsete asemel
        algusedKraadid =-kraadinurk;
        if (activityRunning) {
            loe.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
