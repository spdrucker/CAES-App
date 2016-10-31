package com.music.cornell.music;

/**
 * Created by dantech on 10/24/16.
 */

public class Circle implements Location {

    private double x;
    private double y;
    private double radius;

    public Circle(String[] l){
        String[] p1 = l[0].substring(1,l[0].length()-1).split(",");
        this.x = Double.parseDouble(p1[0]);
        this.y = Double.parseDouble(p1[1]);
        this.radius = Double.parseDouble(l[1]);
    }

    @Override
    public boolean isInLocation(double lat, double lng) {

        return (LocationHolder.degreesLatToMeters(this.x-lat)*(this.x-lat))+LocationHolder.degreesLngToMeters((this.y-lng)*(this.y-lng)) < this.radius*this.radius;
    }
}
