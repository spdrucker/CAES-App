package com.music.cornell.music;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // TODO fix multiple songs being out of sync

    // TODO lock rotation to portrait
    // TODO Don't restart when switching buildings, put loop creator inside new campus settor

    // indicies:
    // 0 is central sounds
    // 1 is ag quad
    // 2 is eng quad
    // 3 is north
    // 4 is west

    private GPSInterface gps;
    private TextView gps_output;
    private MapView mapView;
    private Handler textHandler;
    private Handler redrawHandler;
    private String gpsText;
    private Place lastPlace = null;
    private double[][] intensitiesTo;
    private double[][] intensitiesAt;
    private MediaPlayer[][] sounds;

    private Timer currentTimer;

    private double loopSeconds[] = {236.31,10.0,10.0,10.0,10.0};

    private int currentCampus = -1;

    private int numCampuses = 5;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gps = new GPSInterface(this, this);

        final LocationHolder[] locations = new LocationHolder[numCampuses];
        locations[0] = new LocationHolder(this, R.raw.central_data_songs, "Average radius", "Latitude", "Longitude", "Building");
        locations[1] = new LocationHolder(this, R.raw.ag_quad_data, "Radius", "Latitude", "Longitude", "Building");
        locations[2] = new LocationHolder(this, R.raw.eng_quad_data, "Radius", "Latitude", "Longitude", "Building");
        locations[3] = new LocationHolder(this, R.raw.north_data, "Radius", "Latitude", "Longitude", "Building");
        locations[4] = new LocationHolder(this, R.raw.west_data, "Radius", "Latitude", "Longitude", "Building");

        sounds = new MediaPlayer[numCampuses][];
        sounds[0] = MediaFactory.createCentralSounds(this);

        final String[][] soundNames = new String[numCampuses][];
        soundNames[0] = MediaFactory.getCentralSoundNames();

        intensitiesTo = new double[numCampuses][];
        intensitiesAt = new double[numCampuses][];

        for(int i = 0; i < sounds.length; i++){
            if(sounds[i] != null) {
                // intialize all intensities to 0s
                intensitiesAt[i] = new double[sounds[i].length];
                intensitiesTo[i] = new double[sounds[i].length];
            } else {
                intensitiesAt[i] = new double[0];
                intensitiesTo[i] = new double[0];
            }
        }

        gpsText = "NA";

        gps_output = (TextView)findViewById(R.id.gps_info);

        mapView = (MapView) findViewById(R.id.mapView);
        for(int i = 0; i < locations.length; i++) {
            mapView.addLocation(locations[i]);
        }

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mapView.getVisibility() == View.INVISIBLE) {
                    mapView.setVisibility(View.VISIBLE);
                } else {
                    mapView.setVisibility(View.INVISIBLE);
                }
            }
        });

        mapView.setVisibility(View.INVISIBLE);

        textHandler = new Handler() {
            public void handleMessage(Message msg) {
                System.out.println(gpsText);
                gps_output.setText(gpsText);
            }
        };

        redrawHandler = new Handler() {
            public void handleMessage(Message msg) {
                mapView.invalidate();
            }
        };

        final MainActivity mainActivity = this;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                double[] pos = gps.getPosition();
                mapView.setLat(pos[0]);
                mapView.setLng(pos[1]);
                redrawHandler.obtainMessage(1).sendToTarget();

                // loop through all locations
                for(int i = 0; i < locations.length; i++) {
                    Place p = locations[i].getCurrentPlace(pos[0], pos[1]);
                    if(p != null) {
                        String buildingName = p.getValue(locations[i].columnIndex("Building"));
                        gpsText = "Location: " + pos[0] + "," + pos[1] + "\nCurrently In: " + (p == null ? "NA" : buildingName);
                        textHandler.obtainMessage(1).sendToTarget();

                        // set the new target intensities
                        for(int j = 0; j < intensitiesTo[i].length; j++) {
                            intensitiesTo[i][j] = p.getValueAsDouble(locations[i].columnIndex(soundNames[i][j]));
                        }

                        if(lastPlace == null || lastPlace != p) {
                            lastPlace = p;

                            // if we have the sounds for this campus
                            if(sounds[i] != null) {
                                fadeSoundsTo(i);
                            }
                        }
                        break;
                    }
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 0, 1000);

    }

    public void fadeSoundsTo(int newCamp) {
        if(newCamp != currentCampus) {
            if(currentTimer != null) {
                currentTimer.cancel();
            }

            if(currentCampus != -1){
                // set the current campus target intensities to 0
                for(int i = 0; i < sounds[currentCampus].length; i++) {
                    intensitiesTo[currentCampus][i] = 0;
                }

                System.out.println("fading current");
                // fade out the current campus songs
                fadeCurrentSounds();

                // stop the current campus sounds
                for(int i = 0; i < sounds[currentCampus].length; i++) {
                    sounds[currentCampus][i].stop();
                }
            }

            currentCampus = newCamp;

            // start the new campus sounds
            for(int i = 0; i < sounds[currentCampus].length; i++) {
                // sounds[currentCampus][i].start();
                sounds[currentCampus][i].setLooping(false);
                sounds[currentCampus][i].setVolume(0,0);
            }

            // set the new looper
            TimerTask loopTask = new TimerTask() {
                @Override
                public void run() {
                    for (int j = 0; j < sounds[currentCampus].length; j++) {
                        System.out.println(sounds[currentCampus][j].getDuration()+","+sounds[currentCampus][j].getCurrentPosition());
                        sounds[currentCampus][j].start();
                        sounds[currentCampus][j].seekTo(0);
                    }
                }
            };

            currentTimer = new Timer();
            currentTimer.schedule(loopTask, 0, (int) (loopSeconds[currentCampus]*1000));
        }

        fadeCurrentSounds();
    }

    // fade the current sounds to their necessary positions
    private void fadeCurrentSounds() {
        for(int vol = 0; vol < 100; vol++) {
            for(int i = 0; i < sounds[currentCampus].length; i++) {
                if(intensitiesAt[currentCampus][i] < intensitiesTo[currentCampus][i]) {
                    intensitiesAt[currentCampus][i]+=0.01;
                } else if(intensitiesAt[currentCampus][i] > intensitiesTo[currentCampus][i]) {
                    intensitiesAt[currentCampus][i]-=0.01;
                }
                sounds[currentCampus][i].setVolume((float) intensitiesAt[currentCampus][i], (float) intensitiesAt[currentCampus][i]);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
