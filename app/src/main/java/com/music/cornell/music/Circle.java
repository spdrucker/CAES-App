package com.music.cornell.music;

/**
 * Created by dantech on 10/24/16.
 */

public class Circle implements Location {

    public Circle(float x, float y, float r){

    }

    @Override
    public boolean isInLocation(float lat, float lng) {
        return false;
    }
}
