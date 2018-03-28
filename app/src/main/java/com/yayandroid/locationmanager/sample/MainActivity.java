package com.yayandroid.locationmanager.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yayandroid.locationmanager.sample.activity.KalManMapActivity;
import com.yayandroid.locationmanager.sample.activity.SampleActivity;
import com.yayandroid.locationmanager.sample.activity.SampleMapActivity;
import com.yayandroid.locationmanager.sample.fragment.SampleFragmentActivity;
import com.yayandroid.locationmanager.sample.service.SampleServiceActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void inActivityClick(View view) {
        startActivity(new Intent(this, SampleActivity.class));
    }

    public void inFragmentClick(View view) {
        startActivity(new Intent(this, SampleFragmentActivity.class));
    }

    public void inServiceClick(View view) {
        startActivity(new Intent(this, SampleServiceActivity.class));
    }
    public void inMapClick(View view) {
        startActivity(new Intent(this, SampleMapActivity.class));
    }

    public void inKalMapClick(View view) {
        startActivity(new Intent(this, KalManMapActivity.class));
    }
}
