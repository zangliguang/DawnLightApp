package com.liguang.dawnlightapp;

import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.youku.player.YoukuPlayerBaseApplication;


/**
 * Created by zangliguang on 15/12/19.
 */
//public class DawnLightApplication extends Application {
public class DawnLightApplication extends YoukuPlayerBaseApplication {
    private static DawnLightApplication instance;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
//        FlowManager.init(getApplicationContext());
        refWatcher = LeakCanary.install(this);
    }

    @Override
    public String configDownloadPath() {
        return null;
    }

    // 单例模式获取唯一的MyApplication实例
    public static DawnLightApplication getInstance() {
        if (null == instance) {
            instance = new DawnLightApplication();
        }
        return instance;
    }

    public static RefWatcher getRefWatcher(Context context) {
        DawnLightApplication application = (DawnLightApplication) context.getApplicationContext();
        return application.refWatcher;
    }
}
