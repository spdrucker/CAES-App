package com.music.cornell.music;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

        Paint paint = new Paint();

        if(scale == 0.0f) {
            scale = Math.max(canvas.getWidth(), canvas.getHeight())/1000.0f;
//            System.out.println("scale:" + scale);
        }

        double canvasXCent = canvas.getWidth()/2;
        double canvasYCent = canvas.getHeight()/2;

        if(this != null && this.locations != null) {
            for (int i = 0; i < this.locations.size(); i++) {
                LocationHolder l = this.locations.get(i);
                for (int j = 0; j < l.getLocations().size(); j++) {
                    double latDiff = l.getLocations().get(j).getValueAsDouble(l.getLatColumn()) - lat;
                    double lngDiff = l.getLocations().get(j).getValueAsDouble(l.getLongColumn()) - lng;
                    double rad = l.getLocations().get(j).getValueAsDouble(l.getRadiusColumn());

                    latDiff *= degreesToPixels;
                    lngDiff *= degreesToPixels;
                    rad *= degreesToPixels;

                    latDiff *= scale;
                    lngDiff *= scale;
                    rad *= scale;

                    paint.setColor(Color.GREEN);
                    drawOval(canvas, paint, lngDiff + canvasXCent, canvasYCent - latDiff, rad);
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(28.0f);
                    canvas.drawText(l.getLocations().get(j).getValue(l.getNameColumn()), (float) (lngDiff + canvasXCent - rad), (float) (canvasYCent - latDiff), paint);
                }
            }
        }

        paint.setColor(Color.RED);
        double radius = 20*scale;
        drawOval(canvas, paint, canvasXCent, canvasYCent, radius);

    }

    private void drawOval(Canvas canvas, Paint paint, double xCent, double yCent, double rad) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            canvas.drawOval((float) (xCent-rad), (float) (yCent-rad), (float) (xCent+rad), (float) (yCent+rad), paint);
        } else{
            RectF mRectF = new RectF((float) (xCent-rad), (float) (yCent-rad), (float) (xCent+rad), (float) (yCent+rad));
            canvas.drawOval(mRectF, paint);
        }
    }

    public void setLat(double l) {
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
