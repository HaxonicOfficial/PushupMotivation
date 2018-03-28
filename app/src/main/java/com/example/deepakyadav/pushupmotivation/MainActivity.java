package com.example.deepakyadav.pushupmotivation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import android.speech.tts.TextToSpeech;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor sensor;
    TextToSpeech t1;
    View v;
    Random random;
    private static final int SENSOR_SENSITIVITY=2;
    public final String colors[]={"#f44336","#e91e63","#9c27b0","#673ab7","#3f51b5","#2196f3","#03a9f4","#00bcd4",
            "#009688","#4caf50","#8bc34a", "#cddc39","#ffeb3b","#ffc107","#ff9800","#ff5722","#4e342e","#607d8b",
            "#b71c1c","#880e4f","#4a148c","#311b92","#283593","#00695c","#558b2f","#f57f17","#ffc400","#f57c00",
            "#ff3d00","#263238"};
    public int currentColor;

    public TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        v=findViewById(R.id.mainActivity);
        random=new Random();
        textView=findViewById(R.id.textView);

        if(savedInstanceState!=null){
            textView.setText(savedInstanceState.getString("currentCount"));
        }else{
            textView.setText("-1");
        }
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentCount", textView.getText().toString());
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY){
            if(sensorEvent.values[0]<=SENSOR_SENSITIVITY ){
                //Toast.makeText(this, "near", Toast.LENGTH_SHORT).show();
            }else{
                int score=Integer.parseInt(textView.getText().toString());
                score++;
                textView.setText(score+"");
                currentColor=random.nextInt(colors.length);
                v.setBackgroundColor(Color.parseColor(colors[currentColor]));
                t1.speak(textView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null,null);

            }
        }

    }
    public void reset(View view){
        textView.setText("0");
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
