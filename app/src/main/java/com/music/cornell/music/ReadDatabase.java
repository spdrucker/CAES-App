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

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        return null;
    }
}
