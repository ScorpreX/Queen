package hu.unideb.inf.mobilh12.queen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor beatSensor;
    private long lastBeatTime;
    private MediaPlayer mediaPlayer;
    private TextView tvShakeTimes;
    private int shakesTime = 0;
    private ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        lastBeatTime = 0;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        beatSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);


        tvShakeTimes = findViewById(R.id.shakeTimes);
        if (beatSensor != null) {
            sensorManager.registerListener(this, beatSensor, sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, beatSensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(mediaPlayer == null)
            mediaPlayer = MediaPlayer.create(this, R.raw.song);

        if(constraintLayout == null)
            constraintLayout = findViewById(R.id.activityId);

        long currentTime = System.currentTimeMillis();
        double x_axis = sensorEvent.values[0];
        double y_axis = sensorEvent.values[1];
        double z_axis = sensorEvent.values[2];

        double x = Math.pow(x_axis, 2);
        double y = Math.pow(y_axis, 2);
        double z = Math.pow(z_axis, 2);

        double acc = Math.sqrt(x + y + z);
        tvShakeTimes.setText("Rázások száma: "+ shakesTime);


        if (currentTime - lastBeatTime > 400) {
            if (acc > 3f) {
                if(!mediaPlayer.isPlaying())
                {
                    mediaPlayer.start();
                }
                lastBeatTime = currentTime;
                constraintLayout.setBackgroundColor(Color.parseColor("#FF0000"));
                shakesTime++;
            } else {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}