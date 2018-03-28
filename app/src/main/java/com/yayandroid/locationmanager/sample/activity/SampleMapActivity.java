package com.yayandroid.locationmanager.sample.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.yayandroid.locationmanager.base.LocationBaseActivity;
import com.yayandroid.locationmanager.configuration.Configurations;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;
import com.yayandroid.locationmanager.sample.R;
import com.yayandroid.locationmanager.sample.SamplePresenter;

import java.util.ArrayList;
import java.util.List;

public class SampleMapActivity extends LocationBaseActivity
        implements SamplePresenter.SampleView, OnMapReadyCallback {
    // private ProgressDialog progressDialog;


    private GoogleMap mGoogleMap;
    private List<LatLng> positions=new ArrayList<>();
    private LatLng lastPosition;//=new LatLng(9.958289, 76.358355);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_map);

        getLocation();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return Configurations.defaultConfiguration("Gimme the permission!", "Would you mind to turn GPS on?");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("onLocationChanged","value  :"+location.getLatitude()+","+location.getLongitude());

        //positions.add(new LatLng(location.getLatitude(),location.getLongitude()));

//        if(positions.size()>1)
//        addOnMap(positions);
        addOnMap(new LatLng(location.getLatitude(),location.getLongitude()));

    }

    private void addOnMap(List<LatLng> positions ){
        if(mGoogleMap!=null){

            mGoogleMap.clear();
            PolylineOptions polygonOptions = new PolylineOptions();
            polygonOptions.addAll(positions);
            polygonOptions.color(Color.BLUE);

            Polyline mPolyline = mGoogleMap.addPolyline(polygonOptions);
          /*  CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(positions.get(1)).zoom(8).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/

//            LatLngBounds.Builder b = new LatLngBounds.Builder();
//            for (LatLng m : positions) {
//                b.include(m);
//            }
//            LatLngBounds bounds = b.build();
//
//
//            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }

    private void addOnMap(LatLng position){
        if(mGoogleMap!=null&&lastPosition!=null){
            PolylineOptions polygonOptions = new PolylineOptions();
            polygonOptions.add(lastPosition,position);
            polygonOptions.color(Color.BLUE);
            polygonOptions.width(1);

            mGoogleMap.addPolyline(polygonOptions);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position).zoom(18).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(position));

        }
        lastPosition=position;
    }



    @Override
    public void onLocationFailed(@FailType int failType) {
    }

    @Override
    public void onProcessTypeChanged(@ProcessType int processType) {
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public String getText() {
        return null;
    }

    @Override
    public void setText(String text) {

    }

    @Override
    public void updateProgress(String text) {

    }

    @Override
    public void dismissProgress() {

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
}
