package com.liguang.dawnlightapp.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ListAdapter;

import com.liguang.dawnlightapp.DawnLightApplication;
import com.liguang.dawnlightapp.R;
import com.liguang.dawnlightapp.ui.adapter.ImageListFragmentAdapter;
import com.nshmura.recyclertablayout.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ImageFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    protected RecyclerTabLayout mRecyclerTabLayout;
    /**
     * The fragment's ListView/GridView.
     */
    List<ImageListFragment> fragmentList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ImageFragment() {
    }

    // TODO: Rename and change types of parameters
    public static ImageFragment newInstance(String param1, String param2) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_content;
    }


    @Override
    protected void initView() {
        if (null != fragmentList && fragmentList.size() > 0) {
            return;
        }
        String[] BeautyTitles = getContext().getResources().getStringArray(R.array.beauty_image_titles);
        List<String> items = Arrays.asList(BeautyTitles);
        fragmentList = new ArrayList<>();
        for (int position = 0; position < items.size(); position++) {
            ImageListFragment fragment = new ImageListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ImageListFragment.EXTRA_POSITION, position);
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }


        ImageListFragmentAdapter adapter = new ImageListFragmentAdapter(getFragmentManager(), fragmentList);
        adapter.addAll(items);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(fragmentList.size() - 1);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragmentList.get(position).initData();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRecyclerTabLayout = (RecyclerTabLayout)
                findViewById(R.id.recycler_tab_layout);
        mRecyclerTabLayout.setUpWithViewPager(viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DawnLightApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onTabReselect() {
        super.onTabReselect();
    }
}
