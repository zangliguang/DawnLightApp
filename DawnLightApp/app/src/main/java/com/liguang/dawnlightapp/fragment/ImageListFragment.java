package com.liguang.dawnlightapp.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liguang.dawnlightapp.R;
import com.liguang.dawnlightapp.adapter.ImageDetailAdapter;
import com.liguang.dawnlightapp.db.DawnLightSQLiteHelper;
import com.liguang.dawnlightapp.db.dao.ImageDetailModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangliguang on 15/12/21.
 */
public class ImageListFragment extends Fragment {

    public static final String EXTRA_POSITION = "EXTRA_POSITION";

    DawnLightSQLiteHelper dlsh;
    SQLiteDatabase database;
    Cursor cursor;
    String tableName;
    RecyclerView mRecycleView;
    ImageDetailAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list_view, container, false);
        mRecycleView = (RecyclerView) view.findViewById(R.id.image_recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    protected void initAdapter() {
        setTableName();
        cursor = database.rawQuery("select * from " + tableName + " ORDER BY create_time desc limit ?", new String[]{"1000"});
        Log.v("zangliguang", cursor.getCount() + "");
        List<ImageDetailModel> dataList = new ArrayList<>();
        ImageDetailModel imageDetailModel = new ImageDetailModel();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        while (cursor.moveToNext()) {
            imageDetailModel=new ImageDetailModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), null);
            dataList.add(imageDetailModel);
            Log.v("zangliguang", cursor.getString(0) + "==" + cursor.getString(1) + "==" + cursor.getString(2) + "==" + cursor.getString(3));
        }

        mAdapter = new ImageDetailAdapter(dataList);
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
        if (null != database && database.isOpen()) {
            database.close();
        }
        super.onDestroy();
    }

    private void initDB() {
        dlsh = new DawnLightSQLiteHelper(getContext());
        database = dlsh.getWritableDatabase();
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
