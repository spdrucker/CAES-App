package com.music.cornell.music;

import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private GPSInterface gps;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gps = new GPSInterface(this, this);

        LocationHolder locations = LocationHolder.getHolder(this);

        double[] pos = gps.getPosition();
        Place p = locations.getCurrentPlace(pos[0], pos[1]);

        System.out.println(pos[0]+","+pos[1]);
        System.out.println(p.getValue(locations.columnIndex("Building")));

//        double drumIntensity = p.getValueAsDouble(locations.columnIndex("drum1"));
//
        MediaPlayer mp1 = MediaPlayer.create(this, R.raw.sampleaccoustic);
        mp1.start();

    }
}
