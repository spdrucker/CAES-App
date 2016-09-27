package com.music.cornell.music;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;

/**
 * Created by dantech on 9/26/16.
 */

public class GPSInterface {

    private LocationService service;

    private static LocationService instance = null;

    public GPSInterface(Context context, Activity activity) {

        this.service = getLocationManager(context, activity);
    }

    public double[] getPosition() {
        return new double[]{this.service.getLatitude(), this.service.getLongitude()};
    }

    /**
     * Singleton implementation
     *
     * @return
     */
    private static LocationService getLocationManager(Context context, Activity activity) {
        if (instance == null) {
            instance = new LocationService(context, activity);
        }
        return instance;
    }

    private static class LocationService implements LocationListener, OnRequestPermissionsResultCallback {

        //The minimum distance to change updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

        //The minimum time beetwen updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 5 * 1;//1000 * 60 * 1; // 1 minute

        private final static boolean forceNetwork = false;

        private LocationManager locationManager;
        private double longitude;
        private double latitude;
        private boolean isGPSEnabled;
        private boolean isNetworkEnabled;
        private boolean locationServiceAvailable;

        private final int REQUEST_LOCATION = 1;

        private Context mContext;
        private Activity mActivity;

        /**
         * Local constructor
         */
        private LocationService(Context context, Activity activity) {
            this.mContext = context;
            this.mActivity = activity;

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }

            initLocationService();
            System.out.println("LocationService created");
        }


        /**
         * Sets up location service after permissions is granted
         */
        @TargetApi(23)
        private void initLocationService() {

            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(this.mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this.mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            try {
                this.longitude = 0.0;
                this.latitude = 0.0;
                this.locationManager = (LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);

                // Get GPS and network status
                this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                System.out.printf("Network Status:    gps: %b    network: %b\n", this.isGPSEnabled, this.isNetworkEnabled);

                if (forceNetwork) isGPSEnabled = false;

                if (!isNetworkEnabled && !isGPSEnabled) {
                    // cannot get location
                    this.locationServiceAvailable = false;
                } else {
                    this.locationServiceAvailable = true;

                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        updateCoordinates(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
                    }

                    if (isGPSEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        updateCoordinates(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error creating location service: " + ex.getMessage() + "," + ex.getLocalizedMessage());

            }
        }

        private void updateCoordinates(Location location) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();

            System.out.printf("New coords: %.12f lat, %.12f long\n", this.latitude, this.longitude);
        }

        public double getLatitude(){
            return this.latitude;
        }

        public double getLongitude(){
            return this.longitude;
        }

        public boolean isActive() {
            return this.locationServiceAvailable;
        }


        @Override
        public void onLocationChanged(Location location) {
            // do stuff here with location object
            System.out.println("Location changed: " + location.getProvider());
            System.out.println("Accuracy: "+location.getAccuracy());
            System.out.println("Altitude: "+location.getAltitude());
            updateCoordinates(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            System.out.println("Status changed: " + s + "," + i + "," + bundle.toString());
        }

        @Override
        public void onProviderEnabled(String s) {
            System.out.println("Provider Enabled: " + s);
        }

        @Override
        public void onProviderDisabled(String s) {
            System.out.println("Provider Disabled: " + s);
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == this.REQUEST_LOCATION) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                initLocationService();
            }
        }
    }

}