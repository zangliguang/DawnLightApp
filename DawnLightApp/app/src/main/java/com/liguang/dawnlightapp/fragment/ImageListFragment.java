package com.liguang.dawnlightapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liguang.dawnlightapp.R;
import com.liguang.dawnlightapp.activity.image.ImageViewPageActivity;
import com.liguang.dawnlightapp.db.DawnLightSQLiteHelper;
import com.liguang.dawnlightapp.db.dao.ImageDetailModel;
import com.liguang.dawnlightapp.ui.adapter.ImageDetailAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zangliguang on 15/12/21.
 */
public class ImageListFragment extends BaseFragment {

    public static final String EXTRA_POSITION = "EXTRA_POSITION";

    DawnLightSQLiteHelper dlsh;
    SQLiteDatabase database;
    Cursor cursor;
    String tableName;
    UltimateRecyclerView mRecycleView;
    ImageDetailAdapter simpleRecyclerViewAdapter;
    fragmentListener mListener;
    int index = 0;
    int pageContentNum = 30;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list_view, container, false);
        mRecycleView = (UltimateRecyclerView) view.findViewById(R.id.ultimate_recycler_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.enableLoadmore();
        mRecycleView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                final List<ImageDetailModel> list = loadData();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Iterator<ImageDetailModel> it = list.iterator();

                        while (it.hasNext()) {
                            ImageDetailModel value = it.next();
                            simpleRecyclerViewAdapter.insert(value, simpleRecyclerViewAdapter.getAdapterItemCount());
                        }
                    }
                }, 1000);
//                mAdapter.insert(loadData());
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        mRecycleView.setAdapter(simpleRecyclerViewAdapter);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());

        simpleRecyclerViewAdapter.setCustomLoadMoreView(LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_bottom_progressbar, null));
        simpleRecyclerViewAdapter.setOnItemClickListener(new ImageDetailAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                cursor = database.rawQuery("select * from " + tableName + "  where image_title = ? order by image_order asc",new String[]{data});
                String urls="";
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                }
                do {
                    urls=urls+cursor.getString(3)+",";
                    Log.v("zangliguang", urls);
                }while (cursor.moveToNext());
                Intent it = new Intent(getActivity(), ImageViewPageActivity.class);
                it.putExtra(ImageViewPageActivity.URL_ARGUMENTS_EXTRA, urls);
                getActivity().startActivity(it);
            }
        });

    }

    protected void initAdapter() {
        setTableName();
        simpleRecyclerViewAdapter = new ImageDetailAdapter(loadData());

    }

    private List<ImageDetailModel> loadData() {
        List<ImageDetailModel> dataList = new ArrayList<>();
        cursor = database.rawQuery("select * from " + tableName + " group by image_title ORDER BY create_time desc limit ?,30", new String[]{String.valueOf(index*pageContentNum+index)});
        Log.v("zangliguang", cursor.getCount() + "");
        index++;
        ImageDetailModel imageDetailModel = new ImageDetailModel();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        do {
            imageDetailModel = new ImageDetailModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), null);
            dataList.add(imageDetailModel);
            Log.v("zangliguang", cursor.getString(0) + "==" + cursor.getString(1) + "==" + cursor.getString(2) + "==" + cursor.getString(3));
        }while (cursor.moveToNext());
        return dataList;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initDB();
    }

    @Override
    public void onDestroy() {
        if (null != cursor && !cursor.isClosed()) {
            cursor.close();
        }
        super.onDestroy();
    }

    private void initDB() {
        try {
            mListener = (fragmentListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
        database = mListener.getDatabase();
    }

    private void setTableName() {
        int position = getArguments().getInt(EXTRA_POSITION);
        switch (position) {
            case 0:
                tableName = "image_east_beauty";
                break;
            case 1:
                tableName = "image_western_beauty";
                break;
            case 2:
                tableName = "image_self_timer";
                break;
            case 3:
                tableName = "image_secretly_shooting";
                break;
            case 4:
                tableName = "image_chinese_nude";
                break;
            case 5:
                tableName = "image_asia_six";
                break;
            case 6:
                tableName = "image_silk_stockings";
                break;
            case 7:
                tableName = "image_uniforms_temptation";
                break;
            case 8:
                tableName = "image_star";
                break;
            default:
                tableName = "image_self_timer";
                break;

        }
    }

}
