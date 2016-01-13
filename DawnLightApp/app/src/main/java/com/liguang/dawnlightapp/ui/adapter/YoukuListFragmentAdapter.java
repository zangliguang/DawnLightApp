package com.liguang.dawnlightapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.liguang.dawnlightapp.fragment.youku.YoukuListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangliguang on 15/12/21.
 */
public class YoukuListFragmentAdapter extends FragmentPagerAdapter {
    private List<String> mItems = new ArrayList<>();
    private List<YoukuListFragment> fragmentList = new ArrayList<>();

//    public YoukuListFragmentAdapter(FragmentManager fm, List<YoukuListFragment> lists) {
//        super(fm);
//        this.fragmentList = lists;
//    }


    public YoukuListFragmentAdapter(FragmentManager fm, List<String> mItems, List<YoukuListFragment> fragmentList) {
        super(fm);
        this.mItems = mItems;
        this.fragmentList = fragmentList;
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
