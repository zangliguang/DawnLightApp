package com.liguang.dawnlightapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.liguang.dawnlightapp.fragment.ImageListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangliguang on 15/12/21.
 */
public class ImageListFragmentAdapter extends FragmentPagerAdapter {
    private List<String> mItems = new ArrayList<>();
    private List<ImageListFragment> fragmentList = new ArrayList<>();

    public ImageListFragmentAdapter(FragmentManager fm, List<ImageListFragment> lists) {
        super(fm);
        this.fragmentList = lists;
    }

    @Override
    public Fragment getItem(int position) {
        fragmentList.get(position).onTabReselect();
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    public void addAll(List<String> items) {
        mItems = new ArrayList<>(items);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mItems.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
