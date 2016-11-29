package com.music.cornell.music;

import android.content.Context;
import android.location.Location;

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

    private ArrayList<Place> locations;
    private HashMap<String, Integer> columnTitles;

    private double radiusIncrease = 0.0002;

    private String radiusColumn;
    private String latitudeColumn;
    private String longitudeColumn;
    private String nameColumn;

    public LocationHolder(Context ctx, int resourceID, String rCol, String latCol, String lngCol, String nCol){
        try {
            InputStream input = ctx.getResources().openRawResource(resourceID);

            this.radiusColumn = rCol;
            this.latitudeColumn = latCol;
            this.longitudeColumn = lngCol;
            this.nameColumn = nCol;

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            locations = new ArrayList<>();
            columnTitles = new HashMap<>();

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
                if(columns.length > 0) {
                    locations.add(new Place(columns));
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.printf("There was an error reading the file.");
        }
    }

    public ArrayList<Place> getLocations() {
        return this.locations;
    }

    public int getRadiusColumn() {
        return this.columnTitles.get(this.radiusColumn);
    }

    public int getLatColumn() {
        return this.columnTitles.get(this.latitudeColumn);
    }

    public int getLongColumn() {
        return this.columnTitles.get(this.longitudeColumn);
    }

    public int getNameColumn() {
        return this.columnTitles.get(this.nameColumn);
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
        double pLat = p.getValueAsDouble(columnTitles.get(this.latitudeColumn));
        double pLng = p.getValueAsDouble(columnTitles.get(this.longitudeColumn));
        double radius = p.getValueAsDouble(columnTitles.get(this.radiusColumn))+radiusIncrease;
        double horzDist = pLng-lng;
        double vertDist = pLat-lat;
        return  horzDist*horzDist+vertDist*vertDist <= radius*radius;
    }

    public int columnIndex(String s) {
        return (columnTitles.get(s) == null ? -1 : columnTitles.get(s));
    }
}
