package com.liguang.dawnlightapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.liguang.dawnlightapp.activity.video.CachedActivity;
import com.liguang.dawnlightapp.activity.video.CachingActivity;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.youku.player.YoukuPlayerBaseConfiguration;


/**
 * Created by zangliguang on 15/12/19.
 */
//public class DawnLightApplication extends Application {
public class DawnLightApplication extends Application {
    public static YoukuPlayerBaseConfiguration configuration;
    private static DawnLightApplication instance;
    private RefWatcher refWatcher;

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

    @Override
    public void onCreate() {
        super.onCreate();
//        FlowManager.init(getApplicationContext());
        refWatcher = LeakCanary.install(this);

        configuration = new YoukuPlayerBaseConfiguration(this) {


            /**
             * 通过覆写该方法，返回“正在缓存视频信息的界面”，
             * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
             * 用户需要定义自己的缓存界面
             */
            @Override
            public Class<? extends Activity> getCachingActivityClass() {
                // TODO Auto-generated method stub
                return CachingActivity.class;
            }

            /**
             * 通过覆写该方法，返回“已经缓存视频信息的界面”，
             * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
             * 用户需要定义自己的已缓存界面
             */

            @Override
            public Class<? extends Activity> getCachedActivityClass() {
                // TODO Auto-generated method stub
                return CachedActivity.class;
            }

            /**
             * 配置视频的缓存路径，格式举例： /appname/videocache/
             * 如果返回空，则视频默认缓存路径为： /应用程序包名/videocache/
             *
             */
            @Override
            public String configDownloadPath() {
                // TODO Auto-generated method stub

                return "/myapp/videocache/";            //举例
            }
        };
    }
}
