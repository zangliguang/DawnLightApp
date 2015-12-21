package com.liguang.dawnlightapp;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;


/**
 * Created by zangliguang on 15/12/19.
 */
public class DawnLightApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(getApplicationContext());
    }
}
