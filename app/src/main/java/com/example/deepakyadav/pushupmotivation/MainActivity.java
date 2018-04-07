package com.example.deepakyadav.pushupmotivation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor sensor;
    TextToSpeech t1;
    View v;
    Random random;
    boolean music=true;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private int reps=0;
    private static final int SENSOR_SENSITIVITY=2;
    public final String colors[]={"#f44336","#e91e63","#9c27b0","#673ab7","#3f51b5","#2196f3","#03a9f4","#00bcd4",
            "#009688","#4caf50","#8bc34a", "#cddc39","#ffeb3b","#ffc107","#ff9800","#ff5722","#4e342e","#607d8b",
            "#b71c1c","#880e4f","#4a148c","#311b92","#283593","#00695c","#558b2f","#f57f17","#ffc400","#f57c00",
            "#ff3d00","#263238"};
    public int currentColor;
    public final String words[]={"you can do more!", "keep it going", "just one more", "come on",
            "push it further","don't give up now.", "that's the spirit"};
    public MediaPlayer mediaPlayer;
    public TextView textView;
    public EditText editTextReps;
    public ImageButton imageButtonIncremener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences("pushMotivation", MODE_PRIVATE);
        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        v=findViewById(R.id.mainActivity);
        random=new Random();
        textView=findViewById(R.id.textView);
        editTextReps=findViewById(R.id.setSize);
        imageButtonIncremener=findViewById(R.id.addRep);
        editor=sharedPreferences.edit();

        if(savedInstanceState!=null){
            textView.setText(savedInstanceState.getString("currentCount"));
            editTextReps.setText(savedInstanceState.getString("currentRep"));

        }else{
            editTextReps.setText(sharedPreferences.getString("SetSize", "10"));
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
    public void toggleMusic(View view){
        if (music==true){
            music=false;
            if(mediaPlayer!=null){
            mediaPlayer.reset();
            }
            mediaPlayer=null;
        }else{
            music=true;
        }
    }
    public void incrementReps(View view){
        Log.i("incrementReps()", "functioncalled");
        int current=Integer.parseInt(editTextReps.getText().toString());
        current+=1;
        Log.i("incrementReps()", "current: "+current);
        editTextReps.setText(current+"");

        Log.i("incrementReps()", "current incremented and set to: "+current);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
        editor.putString("SetSize", editTextReps.getText().toString());
        editor.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentCount", textView.getText().toString());
        outState.putString("currentRep", editTextReps.getText().toString());
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
                if(score+2>=Integer.parseInt(editTextReps.getText().toString())){
                    if(mediaPlayer==null&&music==true){
                        mediaPlayer=MediaPlayer.create(getApplicationContext(), R.raw.jungle);
                        mediaPlayer.start();
                    }
                }

                if(score<=Integer.parseInt(editTextReps.getText().toString())){
                    t1.speak(textView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null,null);
                }
            }
        }
    }

    public void reset(View view){
        textView.setText("0");
        if(mediaPlayer!=null){
            mediaPlayer.reset();
        }
        mediaPlayer=null;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
