package com.liguang.dawnlightapp.fragment.youku;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.liguang.dawnlightapp.DawnLightApplication;
import com.liguang.dawnlightapp.R;
import com.liguang.dawnlightapp.db.DawnLightSQLiteHelper;
import com.liguang.dawnlightapp.db.dao.ImageDetailModel;
import com.liguang.dawnlightapp.fragment.BaseFragment;
import com.liguang.dawnlightapp.ui.adapter.ImageDetailAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

/**
 * Created by zangliguang on 15/12/21.
 */
public class YoukuListFragment extends BaseFragment implements ImageDetailAdapter.SQLOperator {

    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static int pageContentNum = 30;
    DawnLightSQLiteHelper dlsh;
    SQLiteDatabase database;
    Cursor cursor;
    String tableName;
    UltimateRecyclerView mRecycleView;
    ImageDetailAdapter simpleRecyclerViewAdapter;
    fragmentListener mListener;
    int index = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_list_view;
    }

    @Override
    protected void initView() {
        super.initView();

    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DawnLightApplication.getRefWatcher(getActivity()).watch(this);
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onTabReselect() {
        super.onTabReselect();
    }

    @Override
    public void deleteNUll(final ImageDetailModel imageDetailModel, boolean loadmore) {
    }

    @Override
    public void deleteBrowsed(final ImageDetailModel imageDetailModel, boolean loadmore) {

//        Toast.makeText(getContext(),"插入结果是"+resultInsert+",删除结果是"+resultDelete,Toast.LENGTH_SHORT).show();

    }
}
