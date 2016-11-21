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
    private Handler mHandler;
    private String gpsText;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gps = new GPSInterface(this, this);

        final LocationHolder[] locations = new LocationHolder[5];
        locations[0] = new LocationHolder(this, R.raw.central_data, "Average radius", "Latitude", "Longitude");
        locations[1] = new LocationHolder(this, R.raw.ag_quad_data, "Radius", "Latitude", "Longitude");
        locations[2] = new LocationHolder(this, R.raw.eng_quad_data, "Radius", "Latitude", "Longitude");
        locations[3] = new LocationHolder(this, R.raw.north_data, "Radius", "Latitude", "Longitude");
        locations[4] = new LocationHolder(this, R.raw.west_data, "Radius", "Latitude", "Longitude");

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
                for(int i = 0; i < locations.length; i++) {
                    double[] pos = gps.getPosition();
                    Place p = locations[i].getCurrentPlace(pos[0], pos[1]);
                    if(p != null) {
                        String buildingName = p.getValue(locations[i].columnIndex("Building"));
                        gpsText = "Location: " + pos[0] + "," + pos[1] + "\nCurrently In: " + (p == null ? "NA" : buildingName);
                        mHandler.obtainMessage(1).sendToTarget();
                        break;
                    }
                }
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
