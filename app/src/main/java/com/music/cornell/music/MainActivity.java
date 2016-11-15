package com.music.cornell.music;

import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private GPSInterface gps;
    private TextView gps_output;
    private LocationHolder locations;
    private Handler mHandler;
    private String gpsText;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gps = new GPSInterface(this, this);

        locations = LocationHolder.getHolder(this);

        gpsText = "NA";

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                System.out.println(gpsText);
                gps_output.setText(gpsText);
            }
        };

        gps_output = (TextView)findViewById(R.id.gps_info);
//        gps_output.post

        final MainActivity mainActivity = this;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Here");
                double[] pos = gps.getPosition();
                Place p = locations.getCurrentPlace(pos[0], pos[1]);
                gpsText = "Location: "+pos[0]+","+pos[1]+"\nCurrently In: "+(p == null ? "NA" : p.getValue(locations.columnIndex("Building")));
                mHandler.obtainMessage(1).sendToTarget();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 0, 1000);

//        double[] pos = gps.getPosition();
//        Place p = locations.getCurrentPlace(pos[0], pos[1]);
//
//        System.out.println(pos[0]+","+pos[1]);
//        System.out.println(p.getValue(locations.columnIndex("Building")));

//        double drumIntensity = p.getValueAsDouble(locations.columnIndex("drum1"));
//
//        MediaPlayer mp1 = MediaPlayer.create(this, R.raw.sampleaccoustic);
//        mp1.start();

    }
}
