package com.youku.player.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.baseproject.utils.Util;
import com.youku.player.ui.R;

/**
 * 此类为Youku默认Loading控件，直接使用，如需要大Loading圈，则tag属性设置为h,如需要大Loading圈，则tag属性设置为l
 *
 * @author 张宇
 * @version $Id
 * @create-time Jan 16, 2012 3:58:09 PM
 */
public class PlayerLoading extends ImageView {

    private static final int LOADING_L = -1;
    // private static final int LOADING_M = 0;
    private static final int LOADING_H = 1;

    private static final String NAME_SPACE = "http://schemas.android.com/apk/res/android";
    private static final String TAG = "tag";

    private int mLoadingMode;
    private float density = 1.5f;
    private int mLoadingSize = Util.dip2px(48);
    private Context context;

    public PlayerLoading(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.density = context.getResources().getDisplayMetrics().density;
    }

    public PlayerLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        int bg = R.anim.yp_mobile_loading;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PlayerNewLoading);
        String loadingSize = ta.getString(R.styleable.PlayerNewLoading_playerloadingSize);
        if (loadingSize == null) {
            setBackgroundResource(bg);
            ta.recycle();
            return;
        }
        if (loadingSize.equals("large")) {
            mLoadingSize = Util.dip2px(76);
        } else if (loadingSize.equals("small")) {
            mLoadingSize = Util.dip2px(16);
        }
        ta.recycle();
        setBackgroundResource(bg);
    }

    public PlayerLoading(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mLoadingSize, mLoadingSize);
    }

    public void startAnimation() {
        this.post(new Runnable() {

            @Override
            public void run() {
                AnimationDrawable animationDrawable = (AnimationDrawable) PlayerLoading.this
                        .getBackground();
                animationDrawable.start();
            }
        });
    }

    public void stopAnimation() {
        this.post(new Runnable() {

            @Override
            public void run() {
                AnimationDrawable animationDrawable = (AnimationDrawable) PlayerLoading.this
                        .getBackground();
                animationDrawable.stop();
            }
        });
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

}
