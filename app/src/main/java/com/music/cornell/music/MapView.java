package com.music.cornell.music;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by dantech on 11/28/16.
 */

public class MapView extends SurfaceView {

    private double lat = 0.0;
    private double lng = 0.0;
    private ArrayList<LocationHolder> locations;

    private float scale = 0.0f;

    private static double degreesToPixels = 100000;

    public MapView(Context context) {
        super(context);
    }

    public MapView(Context C, AttributeSet attribs){
        super(C, attribs);

        // Other setup code you want here
    }

    public MapView(Context C, AttributeSet attribs, int defStyle){
        super(C, attribs, defStyle);

        // Other setup code you want here
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Paint textPaint = new Paint();

        if(scale == 0.0f) {
            scale = Math.max(canvas.getWidth(), canvas.getHeight())/1000.0f;
            System.out.println(scale);
        }


        textPaint.setColor(Color.GREEN);
        for(int i = 0; i < this.locations.size(); i++) {
            LocationHolder l = this.locations.get(i);
            for(int j = 0; j < l.getLocations().size(); j++) {
                double latDiff = l.getLocations().get(j).getValueAsDouble(l.getLatColumn()) - lat;
                double lngDiff = l.getLocations().get(j).getValueAsDouble(l.getLongColumn()) - lng;
                double rad = l.getLocations().get(j).getValueAsDouble(l.getRadiusColumn());

                latDiff *= degreesToPixels;
                lngDiff *= degreesToPixels;
                rad *= degreesToPixels;

                latDiff *= scale;
                lngDiff *= scale;
                rad *= scale;

                canvas.drawOval((float) (canvas.getWidth()/2+latDiff), (float) (canvas.getHeight()/2+lngDiff), (float) (rad), (float) (rad), textPaint);
            }
        }

        textPaint.setColor(Color.RED);
        canvas.drawOval(canvas.getWidth()/2,canvas.getHeight()/2, 100*scale, 100*scale, textPaint);

        System.out.println("drawing"+lat+","+lng);
    }

    public void setLat(double l) {
        System.out.println("Lat set");
        this.lat = l;
    }

    public void setLng(double l) {
        this.lng = l;
    }

    public void addLocation(LocationHolder l) {
        if(this.locations == null) {
            this.locations = new ArrayList<>();
        }

        this.locations.add(l);
    }
}
