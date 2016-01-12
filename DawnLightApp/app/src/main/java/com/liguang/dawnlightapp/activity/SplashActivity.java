package com.liguang.dawnlightapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.liguang.dawnlightapp.R;
import com.liguang.dawnlightapp.constants.LocalConstants;
import com.liguang.dawnlightapp.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_splash, null);
        setContentView(view);
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(800);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    /**
     * 跳转到...
     */
    private void redirectTo() {
        File dbfile = new File(LocalConstants.Paths.DB_PATH);
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
                    LogUtils.v("已经复制" + count);
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
