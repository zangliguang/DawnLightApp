package com.liguang.dawnlightapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguang.dawnlightapp.R;
import com.liguang.dawnlightapp.db.dao.ImageDetailModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zangliguang on 15/12/27.
 */
public class ImageDetailAdapter extends RecyclerView.Adapter<ImageDetailAdapter.ViewHolder> {
    public ImageDetailAdapter(List<ImageDetailModel> dataList) {
        this.dataList = dataList;

    }

    List<ImageDetailModel> dataList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getImage_title());
        Picasso.with(holder.image.getContext()).load(dataList.get(position).getImage_link()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            image= (ImageView) itemView.findViewById(R.id.pic);
            title= (TextView) itemView.findViewById(R.id.name);
        }
    }
}
