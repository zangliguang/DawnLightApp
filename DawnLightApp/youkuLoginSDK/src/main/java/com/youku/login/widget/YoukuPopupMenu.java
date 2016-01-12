package com.youku.login.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.example.youkuloginsdk.R;


/** 自定义PopupMenu工具类 */
@TargetApi(Build.VERSION_CODES.DONUT)
public class YoukuPopupMenu {

	private Context mContext;
	private LayoutInflater mInflater;

	private PopupWindow mPopupWindow;
	private ListView mItemsView;
	private TextView mHeaderTitleView;
	private OnItemSelectedListener mListener;
	private MenuItemAdapter mAdapter;

	private List<MenuItem> mItems;
	private int mWidth;

	public YoukuPopupMenu(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mWidth = mContext.getResources().getDimensionPixelSize(
				R.dimen.popup_menu_width);
		mItems = new ArrayList<MenuItem>();

		initPopupWindow();

		View contentView = mInflater.inflate(R.layout.popup_menu, null);
		mItemsView = (ListView) contentView.findViewById(R.id.items);
		mHeaderTitleView = (TextView) contentView
				.findViewById(R.id.header_title);

		mItemsView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mListener != null) {
					mListener.onItemSelected(mItems.get(position));
				}
				mPopupWindow.dismiss();
			}
		});

		mPopupWindow.setContentView(contentView);
	}

	/** 初始化配置 */
	private void initPopupWindow() {
		mPopupWindow = new PopupWindow(mContext);
		// 低版本有时候可能报异常,兼容一下
		YoukuPopupHelper.fixPopupWindow(mPopupWindow);

		mPopupWindow.setWidth(mWidth);
		mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);

		mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));

		mPopupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					mPopupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
	}

	/** 根据anchor指定响应位置popup */
	public void showAsDropDown(View anchor) {
		int gravity = YoukuPopupHelper.getGravity(anchor, mWidth);
		showAsDropDown(anchor, gravity);
	}

	/** 根据anchor和gravity指定响应位置popup */
	public void showAsDropDown(View anchor, int gravity) {

		if (anchor == null) {
			throw new IllegalStateException(
					"PopupMenu#showAsDropDown anchor cannot be null.");
		}

		if (mItems.size() == 0) {
			throw new IllegalStateException(
					"PopupMenu#add was not called with a menu item to display.");
		}

		preShow();

		int[] position = YoukuPopupHelper.getPopupPosition(anchor, gravity);
		mPopupWindow.showAtLocation(anchor, gravity, position[0], position[1]);
	}

	/** Show之前操作 */
	private void preShow() {
		if (mAdapter == null) {
			mAdapter = new MenuItemAdapter(mContext, mItems);
			mItemsView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Dismiss the popup menu.
	 */
	public void dismiss() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * Add menu item.
	 * 
	 * @param itemId
	 * @param titleRes
	 * @param iconRes
	 * 
	 * @return item
	 */
	public MenuItem add(int itemId, int titleRes) {
		return add(itemId, mContext.getString(titleRes));
	}

	/** Add menu item. */
	public MenuItem add(int itemId, String title) {
		MenuItem item = new MenuItem();
		item.setItemId(itemId);
		item.setTitle(title);
		mItems.add(item);

		return item;
	}

	/** Clear Items */
	public void clear() {
		if (mItems != null) {
			mItems.clear();

			if (mAdapter != null)
				mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Sets the listener to be called when the window is dismissed.
	 * 
	 * @param onDismissListener
	 *            The listener.
	 */
	public void setOnDismissListener(OnDismissListener onDismissListener) {
		mPopupWindow.setOnDismissListener(onDismissListener);
	}

	/**
	 * Sets the popup menu header's title.
	 * 
	 * @param title
	 */
	public void setHeaderTitle(CharSequence title) {
		mHeaderTitleView.setText(title);
		mHeaderTitleView.setVisibility(View.VISIBLE);
		mHeaderTitleView.requestFocus();
	}

	/**
	 * Change the popup's width.
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		mWidth = width;
		mPopupWindow.setWidth(mWidth);
	}

	/**
	 * Register a callback to be invoked when an item in this PopupMenu has been
	 * selected.
	 * 
	 * @param listener
	 */
	public void setOnItemSelectedListener(OnItemSelectedListener listener) {
		mListener = listener;
	}

	/**
	 * Interface definition for a callback to be invoked when an item in this
	 * PopupMenu has been selected.
	 */
	public interface OnItemSelectedListener {
		public void onItemSelected(MenuItem item);
	}

	private static class MenuItemAdapter extends ArrayAdapter<MenuItem> {

		private LayoutInflater mInflater;

		public MenuItemAdapter(Context context, List<MenuItem> objects) {
			super(context, 0, objects);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.menu_list_item, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			MenuItem item = getItem(position);
			if (item.getIcon() != null) {
				holder.icon.setImageDrawable(item.getIcon());
				holder.icon.setVisibility(View.VISIBLE);
			} else {
				holder.icon.setVisibility(View.GONE);
			}
			holder.title.setText(item.getTitle());

			return convertView;
		}

		static class ViewHolder {
			ImageView icon;
			TextView title;
		}

	}

	public static class MenuItem {

		private int itemId;
		private String title;
		private Drawable icon;
		private Intent intent;

		public void setItemId(int itemId) {
			this.itemId = itemId;
		}

		public int getItemId() {
			return itemId;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}

		public void setIcon(Drawable icon) {
			this.icon = icon;
		}

		public Drawable getIcon() {
			return icon;
		}

		public void setIntent(Intent intent) {
			this.intent = intent;
		}

		public Intent getIntent() {
			return intent;
		}
	}
}
