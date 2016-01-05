package com.liguang.dawnlightapp.ui.adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguang.dawnlightapp.R;
import com.liguang.dawnlightapp.configs.picasso.SamplePicassoFactory;
import com.liguang.dawnlightapp.db.dao.ImageDetailModel;
import com.liguang.dawnlightapp.utils.LogUtils;
import com.marshalchen.ultimaterecyclerview.URLogs;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.animators.internal.ViewHelper;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ImageDetailAdapter extends UltimateViewAdapter<ImageDetailAdapter.ViewHolder> implements View.OnClickListener {
    List<ImageDetailModel> dataList;
    private final Picasso mPicasso;
    private SQLOperator sqlOperator;
    Context context;

    public ImageDetailAdapter(Context context, List<ImageDetailModel> dataList) {
        this.dataList = dataList;
        this.context = context;
        mPicasso = SamplePicassoFactory.getPicasso(context);
    }

    private int mDuration = 300;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = 5;

    private boolean isFirstOnly = true;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private boolean loadImage = true;

    @Override
    public void onBindViewHolder(ImageDetailAdapter.ViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= dataList.size() : position < dataList.size()) && (customHeaderView != null ? position > 0 : true)) {
            holder.title.setText(dataList.get(position).getImage_title());
            if (loadImage) {
                LogUtils.v(dataList.get(position).toString());
                mPicasso.load(dataList.get(position).getImage_link())
                        .placeholder(context.getResources().getDrawable(R.drawable.error_pic))
                        .error(context.getResources().getDrawable(R.drawable.error_pic))
                        .into(holder.image);
            }

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
        //将数据保存在itemView的Tag中，以便点击时进行获取
        if (position < dataList.size()) {
            holder.itemView.setTag(dataList.get(position).getImage_title());
            holder.deleteNull.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqlOperator.deleteNUll(dataList.get(position));
                    remove(position);
                }
            });
            holder.deleteBrowsed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqlOperator.deleteBrowsed(dataList.get(position));
                    remove(position);
                }
            });
        }

    }

    public void remove(int position) {
        remove(dataList, position);
        notifyDataSetChanged();
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
        v.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        Button deleteNull;
        Button deleteBrowsed;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.pic);
            title = (TextView) itemView.findViewById(R.id.name);
            deleteNull = (Button) itemView.findViewById(R.id.delete_null);
            deleteBrowsed = (Button) itemView.findViewById(R.id.delete_browsed);
        }
    }

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public boolean isLoadImage() {
        return loadImage;
    }

    public void setLoadImage(boolean loadImage) {
        this.loadImage = loadImage;
        if (loadImage == true) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public interface SQLOperator {
        public void deleteNUll(ImageDetailModel imageDetailModel);

        public void deleteBrowsed(ImageDetailModel imageDetailModel);
    }


    public void setSqlOperator(SQLOperator sqlOperator) {
        this.sqlOperator = sqlOperator;
    }
}
