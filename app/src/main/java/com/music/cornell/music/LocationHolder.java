package com.music.cornell.music;

/**
 * Created by dantech on 10/24/16.
 */

public class LocationHolder {

    // Singleton pattern
    private static LocationHolder instance;

    public static LocationHolder getHolder(){
        if(LocationHolder.instance == null){
            LocationHolder.instance = new LocationHolder();
        }
        return LocationHolder.instance;
    }

    private LocationHolder(){
        // TODO load locations from db
    }
}
