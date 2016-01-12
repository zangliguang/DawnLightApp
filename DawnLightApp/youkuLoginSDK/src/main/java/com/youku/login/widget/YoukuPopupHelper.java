package com.youku.login.widget;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.PopupWindow;

/** Popup工具类 */
@TargetApi(Build.VERSION_CODES.DONUT)
public final class YoukuPopupHelper {

	/**
	 * PopupWindow在低版本会出现空指针<br/>
	 * 为OnScrollChangedListener增加非空判断
	 * 
	 * @param window
	 */
	public final static void fixPopupWindow(final PopupWindow window) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			try {
				final Field mAnchorField = PopupWindow.class
						.getDeclaredField("mAnchor");
				mAnchorField.setAccessible(true);
				Field mOnScrollChangedListenerField = PopupWindow.class
						.getDeclaredField("mOnScrollChangedListener");
				mOnScrollChangedListenerField.setAccessible(true);

				final OnScrollChangedListener mOnScrollChangedListener = (OnScrollChangedListener) mOnScrollChangedListenerField
						.get(window);

				OnScrollChangedListener newListener = new OnScrollChangedListener() {
					public void onScrollChanged() {
						try {
							WeakReference<?> mAnchor = WeakReference.class
									.cast(mAnchorField.get(window));
							Object anchor = mAnchor != null ? mAnchor.get()
									: null;

							if (anchor == null) {
								return;
							} else {
								mOnScrollChangedListener.onScrollChanged();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};

				mOnScrollChangedListenerField.set(window, newListener);
			} catch (Exception e) {
			}
		}
	}

	/** 根据gravity获取响应位置 */
	public final static int[] getPopupPosition(View anchor, int gravity) {
		int[] position = new int[2];

		int windowWidth = anchor.getRootView().getMeasuredWidth();
		int windowHeight = anchor.getRootView().getMeasuredHeight();

		int anchorWidth = anchor.getMeasuredWidth();
		int anchorHeight = anchor.getMeasuredHeight();

		int[] location = new int[2];
		anchor.getLocationInWindow(location);

		if (Gravity.LEFT == (gravity & Gravity.LEFT)) {
			position[0] = location[0];
		} else if (Gravity.RIGHT == (gravity & Gravity.RIGHT)) {
			position[0] = windowWidth - location[0] - anchorWidth;
		}

		if (Gravity.TOP == (gravity & Gravity.TOP)) {
			position[1] = location[1] + anchorHeight;
		} else if (Gravity.BOTTOM == (gravity & Gravity.BOTTOM)) {
			position[1] = windowHeight - location[1];
		}

		return position;
	}

	/**
	 * 获取对齐方式,popupWidth是 windowWidth2/5
	 * 
	 * @see YoukuPopupHelper#getGravity(View, int)
	 * @param anchor
	 * @return {@link Gravity}
	 */
	public final static int getGravity(View anchor) {
		int windowWidth = anchor.getRootView().getMeasuredWidth();
		return getGravity(anchor, windowWidth * 2 / 5);
	}

	/**
	 * 获取对齐方式
	 * 
	 * @param anchor
	 * @param popupWidth
	 *            anchor右侧在Window的距离如果大于popupWidth就是Gravity.RIGHT
	 * @return {@link Gravity}
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public final static int getGravity(View anchor, int popupWidth) {
		int gravity = 0;

		int windowHeight = anchor.getRootView().getMeasuredHeight();

		int anchorParentWidth = ((View) anchor.getParent()).getMeasuredWidth();
		int anchorWidth = anchor.getMeasuredWidth();
		int anchorHeight = anchor.getMeasuredHeight();
		int anchorLeft = anchor.getLeft();

		// int xPos, yPos;
		int[] location = new int[2];
		anchor.getLocationInWindow(location);
		int anchorX = location[0];
		int anchorY = location[1];

		// anchor位于父控件右侧并且左边空间足够
		if (anchorLeft > anchorParentWidth / 2
				&& anchorX + anchorWidth > popupWidth) {
			gravity |= Gravity.RIGHT;
		} else {
			gravity |= Gravity.LEFT;
		}

		if (anchorY + anchorHeight > windowHeight / 2) {
			gravity |= Gravity.BOTTOM;
		} else {
			gravity |= Gravity.TOP;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			gravity |= Gravity.RELATIVE_LAYOUT_DIRECTION;
		}

		return gravity;
	}

}
