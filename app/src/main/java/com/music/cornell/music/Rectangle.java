package com.music.cornell.music;

/**
 * Created by dantech on 10/24/16.
 */

public class Rectangle implements Location {

    private double ulX;
    private double ulY;
    private double lrX;
    private double lrY;

    public Rectangle(String[] l){
        for(int i = 0; i < l.length; i++){
            l[i] = l[i].substring(1,l[i].length()-1);
        }
        String[] p1 = l[0].split(",");
        this.ulX = Double.parseDouble(p1[0]);
        this.ulY = Double.parseDouble(p1[1]);
        String[] p2 = l[1].split(",");
        this.lrX = Double.parseDouble(p2[0]);
        this.lrY = Double.parseDouble(p2[1]);
    }

    @Override
    public boolean isInLocation(double lat, double lng) {
        return lat >= this.ulX && lng >= this.ulY && lat <= this.lrX && lng <= this.lrY;
    }
}
