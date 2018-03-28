package com.yayandroid.locationmanager.sample.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.yayandroid.locationmanager.sample.KalmanLatLong;
import com.yayandroid.locationmanager.sample.R;
import com.yayandroid.locationmanager.sample.service.LocationUpdateService;

import java.util.ArrayList;
import java.util.List;

import static com.yayandroid.locationmanager.sample.service.LocationUpdateService.ACCURACY_DECAYS_TIME;

/**
 * Created by habeeb on 28/3/18.
 */

public class KalManMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "KalManMapActivity";
    private KalmanLatLong kalmanLatLong = new KalmanLatLong(ACCURACY_DECAYS_TIME);


    private GoogleMap mGoogleMap;
    private LatLng lastPosition;
    private LatLng lastKalPosition;

    private LocationManager mLocationManager;

    private static final int LOCATION_INTERVAL = 7000;
    private static final float LOCATION_DISTANCE = 10f;

    KalManMapActivity.LocationListener[] mLocationListeners = new KalManMapActivity.LocationListener[]{
            new KalManMapActivity.LocationListener(LocationManager.GPS_PROVIDER),
            new KalManMapActivity.LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMyLocationEnabled(true);

//                positions.add(new LatLng(9.9540692,76.3425863));
//                positions.add(new LatLng(9.954302, 76.344528));
//                positions.add(new LatLng(9.955084, 76.347972));
//                positions.add(new LatLng(9.956595, 76.354109));
//                positions.add(new LatLng(9.957760, 76.358376));
//                positions.add(new LatLng(9.958289, 76.358355));
//
//                for (LatLng m : positions) {
//                    addOnMap(m);
//                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
// Show rationale and request permission.
        } else mGoogleMap.setMyLocationEnabled(true);
    }

    private void addLatinOnMap(LatLng position){
        if(mGoogleMap!=null&&lastPosition!=null){
            PolylineOptions polygonOptions = new PolylineOptions();
            polygonOptions.add(lastPosition,position);
            polygonOptions.color(Color.RED);
            polygonOptions.width(1);

            mGoogleMap.addPolyline(polygonOptions);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position).zoom(18).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(position));

        }
        lastPosition=position;
    }

    private void addKalManOnMap(LatLng position){

        if(mGoogleMap!=null&&lastKalPosition!=null){
            PolylineOptions polygonOptions = new PolylineOptions();
            polygonOptions.add(lastKalPosition,position);
            polygonOptions.color(Color.BLUE);
            polygonOptions.width(1);

            mGoogleMap.addPolyline(polygonOptions);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position).zoom(18).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(position));

        }
        lastKalPosition=position;
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

            addKalManOnMap(new LatLng(location.getLatitude(),location.getLongitude()));

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
            addKalManOnMap(new LatLng(kalmanLatLong.get_lat(),kalmanLatLong.get_lng()));


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


}
