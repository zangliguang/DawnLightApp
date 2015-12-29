package com.liguang.dawnlightapp;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;


/**
 * Created by zangliguang on 15/12/19.
 */
public class DawnLightApplication extends Application {
    private static DawnLightApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(getApplicationContext());
    }

    // 单例模式获取唯一的MyApplication实例
    public static DawnLightApplication getInstance() {
        if (null == instance) {
            instance = new DawnLightApplication();
        }
        return instance;
    }
}
