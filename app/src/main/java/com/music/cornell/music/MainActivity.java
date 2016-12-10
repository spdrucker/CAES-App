package com.music.cornell.music;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // TODO fix multiple songs being out of sync

    // done:
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

    private double loopSeconds[] = {236.31,10.0,10.0,236.1,10.0};

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
        locations[2] = new LocationHolder(this, R.raw.eng_quad_test, "Radius", "Latitude", "Longitude", "Building");
        locations[3] = new LocationHolder(this, R.raw.north_data, "Radius", "Latitude", "Longitude", "Building");
        locations[4] = new LocationHolder(this, R.raw.west_campus_with_songs, "Radius", "Latitude", "Longitude", "Building");

        // get all of the sound files
        sounds = new MediaPlayer[numCampuses][];
        sounds[0] = MediaFactory.createCentralSounds(this);
        sounds[2] = MediaFactory.createEngSounds(this);
        sounds[3] = MediaFactory.createNorthSounds(this);
        sounds[4] = MediaFactory.createWestSounds(this);

        // get all of the song names
        final String[][] soundNames = new String[numCampuses][];
        soundNames[0] = MediaFactory.getCentralSoundNames();
        soundNames[2] = MediaFactory.getEngSoundNames();
        soundNames[3] = MediaFactory.getNorthSoundNames();
        soundNames[4] = MediaFactory.getWestSoundNames();

        // initialize the intensity arrays
        intensitiesTo = new double[numCampuses][];
        intensitiesAt = new double[numCampuses][];

        // initialize all intensities to 0s for the existing sounds
        for(int i = 0; i < sounds.length; i++){
            if(sounds[i] != null) {
                intensitiesAt[i] = new double[sounds[i].length];
                intensitiesTo[i] = new double[sounds[i].length];
            } else {
                intensitiesAt[i] = new double[0];
                intensitiesTo[i] = new double[0];
            }
        }

        // the initial status text
        gpsText = "No GPS Data Availible Yet";

        // get the textview holding the status message
        gps_output = (TextView)findViewById(R.id.gps_info);

        // add all of the locations to the map view
        mapView = (MapView) findViewById(R.id.mapView);
        for(int i = 0; i < locations.length; i++) {
            mapView.addLocation(locations[i]);
        }

        // add a listener to the view toggle button to toggle the map view
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

        // hide the map view
        mapView.setVisibility(View.INVISIBLE);

        // a handler to update the gps text
        textHandler = new Handler() {
            public void handleMessage(Message msg) {
                System.out.println(gpsText);
                gps_output.setText(gpsText);
            }
        };

        // update the status text
        textHandler.obtainMessage(1).sendToTarget();

        // a handler to update the map view
        redrawHandler = new Handler() {
            public void handleMessage(Message msg) {
                mapView.invalidate();
            }
        };

        final MainActivity mainActivity = this;

        // new task to query the gps every second and find location.
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                double[] pos = gps.getPosition();
                mapView.setLat(pos[0]);
                mapView.setLng(pos[1]);
                redrawHandler.obtainMessage(1).sendToTarget();

                gpsText = "Location: " + pos[0] + "," + pos[1];

                boolean setPlace = false;

                // loop through all locations and try to find the current location
                for(int i = 0; i < locations.length; i++) {
                    Place p = locations[i].getCurrentPlace(pos[0], pos[1]);
                    if(p != null) {
                        String buildingName = p.getValue(locations[i].columnIndex("Building"));
                        gpsText += "\nCurrently In: " + (p == null ? "NA" : buildingName);

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

                        setPlace = true;
                        break;
                    }
                }

                if(!setPlace) {
                    gpsText += "\nNot in a building.";
                }

                textHandler.obtainMessage(1).sendToTarget();
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
                        if(j == 0) {
                            sounds[currentCampus][0].seekTo(0);
                        } else {
                            sounds[currentCampus][j].seekTo(sounds[currentCampus][0].getCurrentPosition());
                        }
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
