package com.youku.login.share;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youkuloginsdk.R;


/**
 * 分享到第三方app的适配器
 * @author afei
 *
 */
public class ChooserAdapter extends BaseAdapter {

	private List<ShareResolveInfo> data = new ArrayList<ShareResolveInfo>();
	
	private LayoutInflater mInflater;
	private PackageManager pm;
	
	public ChooserAdapter(Context context) {
		
		mInflater = LayoutInflater.from(context);
		pm = context.getPackageManager();
		
	}

	@Override
	public int getCount() {
		if(data != null)
			return data.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(data != null)
			return data.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder viewHolder;
		
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.chooser_dialog_item, null);
			viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.chooser_item_imgIcon);
			viewHolder.textTitle = (TextView) convertView.findViewById(R.id.chooser_item_textTitle);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		ShareResolveInfo resovInfo = data.get(position);
		Drawable icon = resovInfo.resolveInfoDrawable;
		String title = resovInfo.resolveInfoTitle;
		
		viewHolder.imgIcon.setBackgroundDrawable(icon);
		viewHolder.textTitle.setText(title);
		
		return convertView;
	}
	
	public void setData(List<ShareResolveInfo> data){
		if(data != null)
			this.data = data;
	}
	
	static class ViewHolder {
		public ImageView imgIcon;
		public TextView textTitle;
	}

}

