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

    private GPSInterface gps;
    private TextView gps_output;
    private MapView mapView;
    private Handler textHandler;
    private Handler redrawHandler;
    private String gpsText;
    private Place lastPlace = null;
    private double[] intensitiesTo;
    private double[] intensitiesAt;

    private double loopSeconds = 10.0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gps = new GPSInterface(this, this);

        final LocationHolder[] locations = new LocationHolder[5];
        locations[0] = new LocationHolder(this, R.raw.central_data, "Average radius", "Latitude", "Longitude", "Building");
        locations[1] = new LocationHolder(this, R.raw.ag_quad_data, "Radius", "Latitude", "Longitude", "Building");
        locations[2] = new LocationHolder(this, R.raw.eng_quad_data, "Radius", "Latitude", "Longitude", "Building");
        locations[3] = new LocationHolder(this, R.raw.north_data, "Radius", "Latitude", "Longitude", "Building");
        locations[4] = new LocationHolder(this, R.raw.west_data, "Radius", "Latitude", "Longitude", "Building");

        final MediaPlayer[] sounds = new MediaPlayer[3];
        sounds[0] = MediaPlayer.create(this, R.raw.violin_);
        sounds[1] = MediaPlayer.create(this, R.raw.trumpet_);
        sounds[2] = MediaPlayer.create(this, R.raw.cello_);

        intensitiesTo = new double[sounds.length];
        intensitiesAt = new double[sounds.length];

        for(int i = 0; i < sounds.length; i++){
            Audio.startSound(sounds[i]);
            intensitiesAt[i] = 0.0;
        }

        TimerTask loopTask = new TimerTask() {
            @Override
            public void run() {
                for (int j = 0; j < sounds.length; j++) {
                    sounds[j].seekTo(0);
                }
            }
        };

        Timer loopTime = new Timer();
        loopTime.schedule(loopTask, 0, (int) (loopSeconds*1000));

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
                for(int i = 0; i < locations.length; i++) {
                    Place p = locations[i].getCurrentPlace(pos[0], pos[1]);
                    if(p != null) {
                        String buildingName = p.getValue(locations[i].columnIndex("Building"));
                        gpsText = "Location: " + pos[0] + "," + pos[1] + "\nCurrently In: " + (p == null ? "NA" : buildingName);
                        textHandler.obtainMessage(1).sendToTarget();

                        intensitiesTo[0] = p.getValueAsDouble(locations[i].columnIndex("violin"));
                        intensitiesTo[1] = p.getValueAsDouble(locations[i].columnIndex("trumpet"));
                        intensitiesTo[2] = p.getValueAsDouble(locations[i].columnIndex("cello"));

                        if(lastPlace == null || lastPlace != p) {
                            lastPlace = p;

                            for(int vol = 0; vol < 100; vol++) {
                                for (int j = 0; j < sounds.length; j++) {
                                    if(intensitiesAt[j] < intensitiesTo[j]) {
                                        intensitiesAt[j]+=0.01;
                                    } else if(intensitiesAt[j] > intensitiesTo[j]) {
                                        intensitiesAt[j]-=0.01;
                                    }
                                    sounds[j].setVolume((float) intensitiesAt[j], (float) intensitiesAt[j]);
                                }

                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
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
