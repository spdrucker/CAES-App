package com.music.cornell.music;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private GPSInterface gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gps = new GPSInterface(this, this);

        LocationHolder locations = LocationHolder.getHolder(this);

        double[] pos = gps.getPosition();
        Place p = locations.getCurrentPlace(pos[0], pos[1]);

        double drumIntensity = p.getValueAsDouble(locations.columnIndex("drum1"));

        MediaPlayer mp1 = new MediaPlayer();
    }
}
