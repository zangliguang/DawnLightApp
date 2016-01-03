package com.liguang.dawnlightapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.AbsListView;
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
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ImageFragment extends BaseFragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    protected RecyclerTabLayout mRecyclerTabLayout;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;


    // TODO: Rename and change types of parameters
    public static ImageFragment newInstance(String param1, String param2) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ImageFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_content;
    }




    @Override
    protected void initView() {
        String[] BeautyTitles = getContext().getResources().getStringArray(R.array.beauty_image_titles);
        List<String> items = Arrays.asList(BeautyTitles);
        List<Fragment> lists=new ArrayList<>();
        for(int position=0;position<items.size();position++){
            ImageListFragment fragment = new ImageListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ImageListFragment.EXTRA_POSITION, position);
            fragment.setArguments(bundle);
            lists.add(fragment);
        }




        ImageListFragmentAdapter adapter = new ImageListFragmentAdapter(getFragmentManager(),lists);
        adapter.addAll(items);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(lists.size()-1);
        viewPager.setAdapter(adapter);

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
//        if(null!=cursor&&!cursor.isClosed()){
//            cursor.close();
//        }
//        if(null!=database&&database.isOpen()){
//            database.close();
//        }
        super.onDestroy();
        DawnLightApplication.getRefWatcher(getActivity()).watch(this);
    }

}
