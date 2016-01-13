package com.liguang.dawnlightapp.activity.video;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.liguang.dawnlightapp.R;
import com.liguang.dawnlightapp.fragment.youku.YoukuListFragment;
import com.liguang.dawnlightapp.ui.adapter.YoukuListFragmentAdapter;
import com.nshmura.recyclertablayout.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class YoukuVideoActivity extends AppCompatActivity {

    @Bind(R.id.recycler_tab_layout)
    RecyclerTabLayout mRecyclerTabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    List<YoukuListFragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_content);
        ButterKnife.bind(this);

        String[] youkuCategory = getResources().getStringArray(R.array.youku_category_titles);
        List<String> items = Arrays.asList(youkuCategory);

        fragmentList = new ArrayList<>();
        for (int position = 0; position < items.size(); position++) {
            YoukuListFragment fragment = new YoukuListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(YoukuListFragment.EXTRA_POSITION, position);
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }


        YoukuListFragmentAdapter adapter = new YoukuListFragmentAdapter(getSupportFragmentManager(), items, fragmentList);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(fragmentList.size() - 1);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                fragmentList.get(position).initData();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRecyclerTabLayout = (RecyclerTabLayout)
                findViewById(R.id.recycler_tab_layout);
        mRecyclerTabLayout.setUpWithViewPager(viewPager);
    }
}
