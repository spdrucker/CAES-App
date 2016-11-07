package com.music.cornell.music;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dantech on 10/24/16.
 */

public class LocationHolder {

    // Singleton pattern
    private static LocationHolder instance;

    private ArrayList<Place> locations;
    private HashMap<String, Integer> columnTitles;

    public static LocationHolder getHolder(Context ctx){
        if(LocationHolder.instance == null){
            LocationHolder.instance = new LocationHolder(ctx);
        }
        return LocationHolder.instance;
    }

    private LocationHolder(Context ctx){
        try {
            InputStream input = ctx.getResources().openRawResource(R.raw.central_data);

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            locations = new ArrayList<>();

            String line = reader.readLine();
            if (line != null) {
                String[] titles = line.split(",");
                for(int i = 0; i < titles.length; i++) {
                    columnTitles.put(titles[i], i);
                }
            }
            line = reader.readLine();
            while (line != null) {
                String[] columns = line.split(",");
                locations.add(new Place(columns));
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.printf("There was an error reading the file.");
        }
    }

    public Place getCurrentPlace(double lat, double lng) {
        for(Place p : this.locations) {
            if(inLocation(p, lat, lng)) {
                return p;
            }
        }

        return null;
    }

    private boolean inLocation(Place p, double lat, double lng) {
        double pLat = p.getValueAsDouble(columnTitles.get("Latitude"));
        double pLng = p.getValueAsDouble(columnTitles.get("Longitude"));
        double radius = p.getValueAsDouble(columnTitles.get("Average radius"));
        double horzDist = degreesLngToMeters(pLng-lng);
        double vertDist = degreesLatToMeters(pLat-lat);
        return  horzDist*horzDist+vertDist*vertDist <= radius*radius;
    }

    private double degreesLatToMeters(double lat) {
        return lat/111082.97;
    }

    private double degreesLngToMeters(double lng) {
        return lng/82198.98;
    }

    public int columnIndex(String s) {
        return columnTitles.get(s);
    }
}
