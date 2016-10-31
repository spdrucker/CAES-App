package com.music.cornell.music;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dantech on 10/24/16.
 */

public class LocationHolder {

    // Singleton pattern
    private static LocationHolder instance;

    private ArrayList<Place> locations;

    public static LocationHolder getHolder(Context ctx){
        if(LocationHolder.instance == null){
            LocationHolder.instance = new LocationHolder(ctx);
        }
        return LocationHolder.instance;
    }

    private LocationHolder(Context ctx){
        try {
            this.locations = ReadDatabase.parseFile(ctx);
        } catch (IOException e) {
            System.out.printf("There was an error reading the file.");
        }
    }

    public Place getCurrentPlace(double lat, double lng) {
        for(Place p : this.locations) {
            if(p.isInPlace(lat, lng)) {
                return p;
            }
        }

        return null;
    }

    public static double degreesLatToMeters(double lat) {
        return lat/111082.97;
    }

    public static double degreesLngToMeters(double lng) {
        return lng/82198.98;
    }
}
