package com.music.cornell.music;

/**
 * Created by dantech on 9/26/16.
 */

public class GPSInterface {

    private double currentLatitude;
    private double currentLongitude;

    public GPSInterface(){}

    public double[] getPosition(){
        return new double[]{currentLatitude, currentLongitude};
    }
}
