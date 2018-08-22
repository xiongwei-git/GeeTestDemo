package com.example.ted.geetestdemo;

import android.app.Application;

import com.geetest.deepknow.DPAPI;

public class TestApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        DPAPI.getInstance(this, "gt_id_test");
    }
}
