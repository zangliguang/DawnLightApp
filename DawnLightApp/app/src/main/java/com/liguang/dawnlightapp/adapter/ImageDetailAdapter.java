package com.liguang.dawnlightapp.adapter;

import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguang.dawnlightapp.R;
import com.liguang.dawnlightapp.db.dao.ImageDetailModel;
import com.marshalchen.ultimaterecyclerview.URLogs;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.animators.internal.ViewHelper;

import java.util.List;


public class ImageDetailAdapter extends UltimateViewAdapter<ImageDetailAdapter.ViewHolder> {
    List<ImageDetailModel> dataList;

    public ImageDetailAdapter(List<ImageDetailModel> dataList) {
        this.dataList = dataList;
    }
    private int mDuration = 300;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = 5;

    private boolean isFirstOnly = true;

    @Override
    public void onBindViewHolder(ImageDetailAdapter.ViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= dataList.size() : position < dataList.size()) && (customHeaderView != null ? position > 0 : true)) {
            holder.title.setText(dataList.get(position).getImage_title());
        }
        if (!isFirstOnly || position > mLastPosition) {
            for (Animator anim : getAdapterAnimations(holder.itemView, AdapterAnimationType.ScaleIn)) {
                anim.setDuration(mDuration).start();
                anim.setInterpolator(mInterpolator);
            }
            mLastPosition = position;
        } else {
            ViewHelper.clear(holder.itemView);
        }

    }


    @Override
    public int getAdapterItemCount() {
        return dataList.size();
    }

    @Override
    public ImageDetailAdapter.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ImageDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    public void insert(ImageDetailModel string, int position) {
        insert(dataList, string, position);
    }


    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }


    public void swapPositions(int from, int to) {
        swapPositions(dataList, from, to);
    }


    @Override
    public long generateHeaderId(int position) {
        URLogs.d("position--" + position + "   " + getItem(position));
        return -1;
//        if (getItem(position).length() > 0)
//            return getItem(position).charAt(0);
//        else return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stick_header_item, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

//        TextView textView = (TextView) viewHolder.itemView.findViewById(R.id.stick_text);
//        textView.setText(String.valueOf(getItem(position).getImage_title()));
//        viewHolder.itemView.setBackgroundColor(Color.parseColor("#AAffffff"));
//        ImageView imageView = (ImageView) viewHolder.itemView.findViewById(R.id.stick_img);
//
//        SecureRandom imgGen = new SecureRandom();
//        switch (imgGen.nextInt(3)) {
//            case 0:
//                imageView.setImageResource(R.drawable.test_back1);
//                break;
//            case 1:
//                imageView.setImageResource(R.drawable.test_back2);
//                break;
//            case 2:
//                imageView.setImageResource(R.drawable.test_back3);
//                break;
//        }

    }



    public ImageDetailModel getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < dataList.size())
            return dataList.get(position);
        else return null;
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
