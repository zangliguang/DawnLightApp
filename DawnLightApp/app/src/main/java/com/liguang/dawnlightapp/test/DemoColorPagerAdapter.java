package com.liguang.dawnlightapp.test;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liguang.dawnlightapp.R;
import com.liguang.dawnlightapp.ui.adapter.NormalRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DemoColorPagerAdapter extends PagerAdapter {

    private List<String> mItems = new ArrayList<>();

    public DemoColorPagerAdapter() {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView view = (RecyclerView) LayoutInflater.from(container.getContext())
                .inflate(R.layout.layout_page, container, false);
        view.setLayoutManager(new LinearLayoutManager(container.getContext()));//这里用线性显示 类似于listview
        view.setAdapter(new NormalRecyclerViewAdapter(container.getContext()));
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public String getPageTitle(int position) {
        return mItems.get(position);
    }

    public String getColorItem(int position) {
        return mItems.get(position);
    }

    public void addAll(List<String> items) {
        mItems = new ArrayList<>(items);
    }
}
