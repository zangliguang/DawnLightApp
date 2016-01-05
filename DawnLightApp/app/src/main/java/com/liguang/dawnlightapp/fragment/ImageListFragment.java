package com.liguang.dawnlightapp.fragment;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liguang.dawnlightapp.DawnLightApplication;
import com.liguang.dawnlightapp.R;
import com.liguang.dawnlightapp.activity.image.ImageViewPageActivity;
import com.liguang.dawnlightapp.db.DawnLightSQLiteHelper;
import com.liguang.dawnlightapp.db.dao.ImageDetailModel;
import com.liguang.dawnlightapp.ui.adapter.ImageDetailAdapter;
import com.liguang.dawnlightapp.utils.LogUtils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zangliguang on 15/12/21.
 */
public class ImageListFragment extends BaseFragment  implements ImageDetailAdapter.SQLOperator {

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

    @TargetApi(Build.VERSION_CODES.M)
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
                loadMoreData();
            }
        });
        return view;
    }

    protected void refreshData() {
        if(null==simpleRecyclerViewAdapter||simpleRecyclerViewAdapter.getAdapterItemCount()==0){
            loadMoreData();
        }
    }
    protected void loadMoreData() {
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
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        LogUtils.v("ImageListFragment----onViewCreated");
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
        simpleRecyclerViewAdapter = new ImageDetailAdapter(getActivity(), getArguments().getInt(EXTRA_POSITION)==0?loadData():new ArrayList<ImageDetailModel>() );
        simpleRecyclerViewAdapter.setSqlOperator(this);
    }

    private List<ImageDetailModel> loadData() {
        List<ImageDetailModel> dataList = new ArrayList<>();
        cursor = database.rawQuery("select * from " + tableName + " group by image_title ORDER BY create_time asc limit ?,30", new String[]{String.valueOf(index*pageContentNum+index)});
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
        DawnLightApplication.getRefWatcher(getActivity()).watch(this);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onTabReselect() {
        super.onTabReselect();
    }

    @Override
    public void deleteNUll(ImageDetailModel imageDetailModel) {
//        String insertSql="INSERT INTO image_to_delete (image_id, image_title, image_type, image_link, image_order,create_time) VALUES ("+imageDetailModel.getImage_id()+","+" ?, ?, ?, ?,?)";
        ContentValues cValue = new ContentValues();
        cValue.put("image_id",imageDetailModel.getImage_id());
        cValue.put("image_title",imageDetailModel.getImage_title());
        cValue.put("image_type",imageDetailModel.getImage_type());
        cValue.put("image_link",imageDetailModel.getImage_link());
        cValue.put("image_order",imageDetailModel.getImage_order());
        cValue.put("create_time", String.valueOf(imageDetailModel.getCreate_time()));
        long resultInsert=database.insert("image_to_delete", null, cValue);
        int resultDelete=database.delete(tableName,"image_title=?",new String[]{imageDetailModel.getImage_title()});
//        Toast.makeText(getContext(),"插入结果是"+resultInsert+",删除结果是"+resultDelete,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteBrowsed(ImageDetailModel imageDetailModel) {
        ContentValues cValue = new ContentValues();
        cValue.put("image_id",imageDetailModel.getImage_id());
        cValue.put("image_title",imageDetailModel.getImage_title());
        cValue.put("image_type",imageDetailModel.getImage_type());
        cValue.put("image_link",imageDetailModel.getImage_link());
        cValue.put("image_order",imageDetailModel.getImage_order());
        cValue.put("create_time", String.valueOf(imageDetailModel.getCreate_time()));
        long resultInsert=database.insert("image_browsed", null, cValue);
        int resultDelete=database.delete(tableName,"image_title=?",new String[]{imageDetailModel.getImage_title()});
//        Toast.makeText(getContext(),"插入结果是"+resultInsert+",删除结果是"+resultDelete,Toast.LENGTH_SHORT).show();

    }
}
