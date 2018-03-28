package com.yayandroid.locationmanager.sample.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.yayandroid.locationmanager.sample.KalmanLatLong;
import com.yayandroid.locationmanager.sample.SampleApplication;


/**
 * Created by habeeb on 28/3/18.
 */

public class LocationUpdateService  extends Service {


    private static final String TAG = "LocationUpdationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 7000;
    private static final float LOCATION_DISTANCE = 10f;


    public static final int ACCURACY_DECAYS_TIME = 3; // Metres per second

    private KalmanLatLong kalmanLatLong = new KalmanLatLong(ACCURACY_DECAYS_TIME);

    private ValidPosition validPosition = new ValidPosition();



    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {


            initializeLocationManager();
            if (Build.VERSION.SDK_INT >= 23)
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                                mLocationListeners[1]);
                    } catch (java.lang.SecurityException ex) {
                        Log.i(TAG, "fail to request location update, ignore", ex);
                    } catch (IllegalArgumentException ex) {
                        Log.d(TAG, "network provider does not exist, " + ex.getMessage());
                    }
                    try {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                                mLocationListeners[0]);
                    } catch (java.lang.SecurityException ex) {
                        Log.i(TAG, "fail to request location update, ignore", ex);
                    } catch (IllegalArgumentException ex) {
                        Log.d(TAG, "gps provider does not exist " + ex.getMessage());
                    }

                } else {
                    try {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                                mLocationListeners[1]);
                    } catch (java.lang.SecurityException ex) {
                        Log.i(TAG, "fail to request location update, ignore", ex);
                    } catch (IllegalArgumentException ex) {
                        Log.d(TAG, "network provider does not exist, " + ex.getMessage());
                    }
                    try {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                                mLocationListeners[0]);
                    } catch (java.lang.SecurityException ex) {
                        Log.i(TAG, "fail to request location update, ignore", ex);
                    } catch (IllegalArgumentException ex) {
                        Log.d(TAG, "gps provider does not exist " + ex.getMessage());
                    }
                }

        new Thread(new Runnable() {
            public void run() {

              /*  if(!mUserId.equals(0L)   && !mPartnerToken.equals("not")&&!mAccessToken.equals("not")&&!mChatId.equals("not")&&!fcm.equals("not"))
              try{
                  Log.e(TAG,"new Thread(new Runnable() {");
                 setLocation(mUserId,fcm,mPartnerToken,mAccessToken,mChatId);
              }catch (NetworkError r){
                  r.printStackTrace();
              }*/
            }
        }).start();

    }



    @Override
    public void onDestroy() {

        super.onDestroy();
            /*if (mLocationManager != null) {
                for (int i = 0; i < mLocationListeners.length; i++) {
                    try {
                        mLocationManager.removeUpdates(mLocationListeners[i]);
                    } catch (Exception ex) {
                        Log.i(TAG, "fail to remove location listners, ignore", ex);
                    }
                }
            }*/
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }




    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) SampleApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;


        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

            Log.e(TAG, "LocationListener locatoins    Latin   " + mLastLocation.getLatitude() + "LOGi" + mLastLocation.getLatitude());
           /* if(mUserId==0L)
                sendCurrentLocations(getCurrentTime(),mLastLocation.getLatitude(),mLastLocation.getLongitude(),mUserId);
*/
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location.getLatitude()+","+location.getLongitude());

            mLastLocation.set(location);


            if(validPosition.checkPosition(location)) {

                kalmanLatLong.process(
                        location.getLatitude(),
                        location.getLongitude(),
                        location.getAccuracy(),
                        location.getTime()
                );

                location.setLatitude(kalmanLatLong.get_lat());
                location.setLongitude(kalmanLatLong.get_lng());
                location.setAccuracy(kalmanLatLong.get_accuracy());

                Log.e(TAG, "kalmanLatLong: " + kalmanLatLong.get_lat()+","+kalmanLatLong.get_lng());
            }

//                if (isConnected())
//                    Log.e(TAG, "isConnected: " + location);
//                   // sendCurrentLocations(getCurrentTime(), location.getLatitude(), location.getLongitude(), user_id);//// TODO: 4/18/2017    location sending canceled
//                else
//                    Log.e(TAG, "  if(isConnected()) else ");

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }


    public class ValidPosition {

        public boolean checkPosition(Location location) {

//            if (( location != null ) &&
//                    (distance != 0) &&
//                    (distance > MINIMUM_DISTANCE) && // 10 metres
//
//                    (location.hasSpeed()) &&
//                    (location.getSpeed() > 0) &&
//                    (averageTime < HUMANSPEED) &&
//
//                    (location.hasAccuracy()) &&
//                    (location.getAccuracy() < MINIMUM_ACCURACY) &&
//                    (isBetterLocation(location, lastLocation)))// From Google example in http://developer.android.com/guide/topics/location/strategies.html#BestPerformance
//            return true;

            return true;
        }

    }
}