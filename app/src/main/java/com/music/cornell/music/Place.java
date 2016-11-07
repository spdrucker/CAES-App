package com.music.cornell.music;

/**
 * Created by dantech on 10/24/16.
 */

public class Place {

    private String[] values;

    public Place(String[] v) {
        this.values = v;
    }

    public String getValue(int i) {
        return this.values[i];
    }

    public double getValueAsDouble(int i) {
        return Double.parseDouble(this.values[i]);
    }
}
