package com.yayandroid.locationmanager.sample;

import android.app.Application;
import android.content.Context;

import com.yayandroid.locationmanager.LocationManager;

public class SampleApplication extends Application {
    private static SampleApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        LocationManager.enableLog(true);
        mInstance = this;

    }

    public static synchronized SampleApplication getInstance() {
        return mInstance;
    }
}
