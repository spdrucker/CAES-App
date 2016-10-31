package com.music.cornell.music;

/**
 * Created by dantech on 10/24/16.
 */

public class Place {

    private String name;
    private Location location;
    private String campus;
    private String[] instruments;

    public Place(String name, Location location, String campus, String[] instruments) {
        this.name = name;
        this.location = location;
        this.campus = campus;
        this.instruments = instruments;
    }

    public boolean isInPlace(double lat, double lng) {
        return this.location.isInLocation(lat, lng);
    }
}
