package com.liguang.dawnlightapp.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.liguang.dawnlightapp.fragment.ImageListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangliguang on 15/12/21.
 */
public class ImageListFragmentAdapter extends FragmentPagerAdapter {
    private List<String> mItems = new ArrayList<>();

    public ImageListFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        final Bundle bundle = new Bundle();
        bundle.putInt(ImageListFragment.EXTRA_POSITION, position);

        final ImageListFragment fragment = new ImageListFragment();
        fragment.setArguments(bundle);
        return fragment;
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
}
