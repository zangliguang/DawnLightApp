package com.liguang.dawnlightapp.ui;


import com.liguang.dawnlightapp.R;
import com.liguang.dawnlightapp.fragment.ImageFragment;
import com.liguang.dawnlightapp.fragment.MusicFragment;
import com.liguang.dawnlightapp.fragment.NewsFragment;
import com.liguang.dawnlightapp.fragment.TestFragment;
import com.liguang.dawnlightapp.fragment.VideoFragment;

public enum MainTab {

	NEWS(0, R.string.common_news, R.drawable.tab_icon_news,
			NewsFragment.class),

	IMAGE(1, R.string.common_image, R.drawable.tab_icon_image,
			ImageFragment.class),

	VIDEO(2, R.string.common_video, R.drawable.tab_icon_video,
			VideoFragment.class),

	MUSIC(3, R.string.common_music, R.drawable.tab_icon_music,
			MusicFragment.class),

	TEST(4, R.string.common_test, R.drawable.tab_icon_test,
			TestFragment.class);

	private int idx;
	private int resName;
	private int resIcon;
	private Class<?> clz;

	private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
		this.idx = idx;
		this.resName = resName;
		this.resIcon = resIcon;
		this.clz = clz;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getResName() {
		return resName;
	}

	public void setResName(int resName) {
		this.resName = resName;
	}

	public int getResIcon() {
		return resIcon;
	}

	public void setResIcon(int resIcon) {
		this.resIcon = resIcon;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}
}
