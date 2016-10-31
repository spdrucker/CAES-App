package com.music.cornell.music;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by dantech on 10/24/16.
 */

public class ReadDatabase {

    private static String database = "";

    private ReadDatabase(){
        throw new UnsupportedOperationException();
    }

    public static ArrayList<Place> parseFile(Context ctx) throws IOException {
        InputStream input = ctx.getResources().openRawResource(R.raw.data);

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        ArrayList<Place> places = new ArrayList<>();

        String[] lines = new String[4];
        int i = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            lines[i] = line;
            i++;
            if(line.equals("")) {
                // campus

                i = 0;
            } else if(i >= 4) {
                // building
                Location l = null;
                String[] points = lines[1].split(" ");
                if(points[1].startsWith("(")) {
                    l = new Rectangle(points);
                } else {
                    l = new Circle(points);
                }
                places.add(new Place(lines[0], l, lines[2], lines[3].split(" ")));
                i = 0;
            }
        }

        return places;
    }
}
