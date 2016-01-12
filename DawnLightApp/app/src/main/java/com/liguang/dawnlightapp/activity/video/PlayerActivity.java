package com.liguang.dawnlightapp.activity.video;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baseproject.utils.Logger;
import com.liguang.dawnlightapp.R;
import com.youku.player.ApiManager;
import com.youku.player.VideoQuality;
import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;
import com.youku.service.download.DownloadManager;
import com.youku.service.download.OnCreateDownloadListener;

/**
 * 播放器播放界面，
 */
public class PlayerActivity extends Activity {

    private YoukuBasePlayerManager basePlayerManager;
    public View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.biaoqing: // 切换标清
                    change(VideoQuality.STANDARD);
                    break;
                case R.id.gaoqing: // 切换高清
                    change(VideoQuality.HIGHT);
                    break;
                case R.id.chaoqing: // 切换超清
                    change(VideoQuality.SUPER);
                    break;
                case R.id.most: // 切换为1080P
                    change(VideoQuality.P1080);
                    break;
                case R.id.download: // 下载视频接口测试
                    doDownload();
                    break;
            }

        }
    };
    // 播放器控件
    private YoukuPlayerView mYoukuPlayerView;
    // 需要播放的视频id
    private String vid;
    // 清晰度相关按钮
    private Button btn_standard, btn_hight, btn_super, btn_1080;
    // 下载视频按钮
    private Button btn_download;
    // 需要播放的本地视频的id
    private String local_vid;
    // 标示是否播放的本地视频
    private boolean isFromLocal = false;
    private String id = "";
    // YoukuPlayer实例，进行视频播放控制
    private YoukuPlayer youkuPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_play);
        iniView();
        basePlayerManager = new YoukuBasePlayerManager(this) {

            @Override
            public void setPadHorizontalLayout() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onInitializationSuccess(YoukuPlayer player) {
                // TODO Auto-generated method stub
                // 初始化成功后需要添加该行代码
                addPlugins();

                // 实例化YoukuPlayer实例
                youkuPlayer = player;

                // 进行播放
                goPlay();
            }

            @Override
            public void onSmallscreenListener() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFullscreenListener() {
                // TODO Auto-generated method stub

            }
        };
        basePlayerManager.onCreate();

        // 通过上个页面传递过来的Intent获取播放参数
        getIntentData(getIntent());

        if (TextUtils.isEmpty(id)) {
            vid = "XMTQzOTk5NTMwNA"; // 默认视频
        } else {
            vid = id;
        }

        // 播放器控件
        mYoukuPlayerView = (YoukuPlayerView) this
                .findViewById(R.id.full_holder);
        //控制竖屏和全屏时候的布局参数。这两句必填。
        mYoukuPlayerView
                .setSmallScreenLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        mYoukuPlayerView
                .setFullScreenLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        // 初始化播放器相关数据
        mYoukuPlayerView.initialize(basePlayerManager);

    }

    @Override
    public void onBackPressed() { // android系统调用
        Logger.d("sgh", "onBackPressed before super");
        super.onBackPressed();
        Logger.d("sgh", "onBackPressed");
        basePlayerManager.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        basePlayerManager.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        basePlayerManager.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean managerKeyDown = basePlayerManager.onKeyDown(keyCode, event);
        if (basePlayerManager.shouldCallSuperKeyDown()) {
            return super.onKeyDown(keyCode, event);
        } else {
            return managerKeyDown;
        }

    }

    @Override
    public void onLowMemory() { // android系统调用
        super.onLowMemory();
        basePlayerManager.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        basePlayerManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        basePlayerManager.onResume();
    }

    @Override
    public boolean onSearchRequested() { // android系统调用
        return basePlayerManager.onSearchRequested();
    }

    @Override
    protected void onStart() {
        super.onStart();
        basePlayerManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        basePlayerManager.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);

        // 通过Intent获取播放需要的相关参数
        getIntentData(intent);

        // 进行播放
        goPlay();
    }

    /**
     * 获取上个页面传递过来的数据
     */
    private void getIntentData(Intent intent) {

        if (intent != null) {
            // 判断是不是本地视频
            isFromLocal = intent.getBooleanExtra("isFromLocal", false);

            if (isFromLocal) { // 播放本地视频
                local_vid = intent.getStringExtra("video_id");
            } else { // 在线播放
                id = intent.getStringExtra("vid");
            }
        }

    }

    private void goPlay() {
        if (isFromLocal) { // 播放本地视频
            youkuPlayer.playLocalVideo(local_vid);
        } else { // 播放在线视频
            youkuPlayer.playVideo(vid);
        }

        // XNzQ3NjcyNDc2
        // XNzQ3ODU5OTgw
        // XNzUyMzkxMjE2
        // XNzU5MjMxMjcy 加密视频
        // XNzYxNzQ1MDAw 万万没想到
        // XNzgyODExNDY4 魔女范冰冰扑倒黄晓明
        // XNDcwNjUxNzcy 姐姐立正向前走
        // XNDY4MzM2MDE2 向着炮火前进
        // XODA2OTkwMDU2 卧底韦恩突出现 劫持案愈发棘手
        // XODUwODM2NTI0 会员视频
        // XODQwMTY4NDg0 一个人的武林
    }

    private void iniView() {
        btn_standard = (Button) this.findViewById(R.id.biaoqing);
        btn_hight = (Button) this.findViewById(R.id.gaoqing);
        btn_super = (Button) this.findViewById(R.id.chaoqing);
        btn_1080 = (Button) this.findViewById(R.id.most);
        btn_download = (Button) this.findViewById(R.id.download);

        btn_standard.setOnClickListener(listener);
        btn_hight.setOnClickListener(listener);
        btn_super.setOnClickListener(listener);
        btn_1080.setOnClickListener(listener);
        btn_download.setOnClickListener(listener);
    }

    /**
     * 更改视频的清晰度
     *
     * @param quality VideoQuality有四种枚举值：{STANDARD,HIGHT,SUPER,P1080}，分别对应：标清，高清，超清，
     *                1080P
     */

    private void change(VideoQuality quality) {
        try {
            // 通过ApiManager实例更改清晰度设置，返回值（1):成功；（0): 不支持此清晰度
            // 接口详细信息可以参数使用文档
            int result = ApiManager.getInstance().changeVideoQuality(quality,
                    basePlayerManager);
            if (result == 0)
                Toast.makeText(PlayerActivity.this, "不支持此清晰度", 2000).show();
        } catch (Exception e) {
            Toast.makeText(PlayerActivity.this, e.getMessage(), 2000).show();
        }
    }

    /**
     * 简单展示下载接口的使用方法，用户可以根据自己的 通过DownloadManager下载视频
     */
    private void doDownload() {
        // 通过DownloadManager类实现视频下载
        DownloadManager d = DownloadManager.getInstance();
        /**
         * 第一个参数为需要下载的视频id 第二个参数为该视频的标题title 第三个对下载视频结束的监听，可以为空null
         */
        d.createDownload("XNzgyODExNDY4", "魔女范冰冰扑倒黄晓明",
                new OnCreateDownloadListener() {

                    @Override
                    public void onfinish(boolean isNeedRefresh) {
                        // TODO Auto-generated method stub

                    }
                });
    }

}
