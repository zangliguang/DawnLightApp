package com.liguang.dawnlightapp;

import android.app.Application;
import android.content.res.AssetManager;

import com.liguang.dawnlightapp.constants.LocalConstants;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by zangliguang on 15/12/19.
 */
public class DawnLightApplication extends Application {
    private static DawnLightApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(getApplicationContext());
        File dbfile = new File(this.getFilesDir().getAbsolutePath() + "/" + LocalConstants.dbname);
        if (!dbfile.exists()) {
            try {
                AssetManager asset = getAssets();
                InputStream is = null;
                is = asset.open("dawnlight.db");
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[1024];
                int count = 0;
                // 开始复制testDatabase.db文件
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 单例模式获取唯一的MyApplication实例
    public static DawnLightApplication getInstance() {
        if (null == instance) {
            instance = new DawnLightApplication();
        }
        return instance;
    }
}
