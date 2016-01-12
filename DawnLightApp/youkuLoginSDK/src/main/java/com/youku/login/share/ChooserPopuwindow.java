package com.youku.login.share;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.example.youkuloginsdk.R;
import com.youku.analytics.data.Device;
import com.youku.login.util.Logger;

/**
 * 自定义Intent Chooser对话框， 弹出分享到第三方的对话框
 * 
 * @author Luke
 * 
 */
public class ChooserPopuwindow {

	// private AlertDialog dialog;
	private ChooserAdapter adapter;
	private ListView listView;
//	private View footView;
	private boolean isPerformChooserAction;
	private PopupWindow popupWindow;
	private Context context;
	private boolean isFullScreen;
	private int widthFullScreenPopuWindow;
	private int heightFullScreenPopuWindow;
	private boolean isActionBar=false;
	private Dialog mDialog;

	/**
	 * 
	 * 初始化ChooserDialog
	 * 
	 * @Title: ChooserDialog
	 * @param context
	 * @date 2013-5-21 上午10:05:22
	 */
	public ChooserPopuwindow(final View interactView, final View titleLayout,
			final View controlLayout, boolean isFullScreen, Context context,boolean actionbar) {
		this.context = context;
		this.isFullScreen = isFullScreen;
		isActionBar=actionbar;
		View parentView = (View) LayoutInflater.from(context).inflate(
				R.layout.chooser_dialog_list, null);
		listView = (ListView) parentView.findViewById(R.id.popup_listview_cont);
//		footView = (View) LayoutInflater.from(context).inflate(
//				R.layout.chooser_dialog_list_footview, null);
		widthFullScreenPopuWindow = -(Device.ht * 1 / 3);
		heightFullScreenPopuWindow = -(Device.wt * 34) / 100;
//		widthFullScreenPopuWindow = -(Device.WT * 1 / 3);
//		heightFullScreenPopuWindow = -(Device.HT * 32) / 100;
//		listView.addFooterView(footView, null, true);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (onItemSelectedListener != null)
					onItemSelectedListener.onItemSelected(adapter, position);
			}
		});
		mDialog= new Dialog(context,R.style.ShareDialog);
		mDialog.setContentView(parentView);
		mDialog.setCanceledOnTouchOutside(true);
		// popupWindow = new PopupWindow(parentView,
		// (int)(context.getResources().getDimension(R.dimen.PopuWindowWith)),
		// LayoutParams.WRAP_CONTENT);
		int widthPopuWindow = Device.ht>Device.wt?Device.wt:Device.ht;
		Logger.lxf("===弹出的对话框的宽是===Device.ht="+Device.ht);
		Logger.lxf("===弹出的对话框的宽是===Device.wt="+Device.wt);
		Logger.lxf("===弹出的对话框的宽是===widthPopuWindow="+widthPopuWindow);
		
		popupWindow = new PopupWindow(parentView,
				widthPopuWindow,
				LayoutParams.WRAP_CONTENT);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (null != controlLayout) {
					controlLayout.setVisibility(View.GONE);
				}
				if (null != interactView) {
					interactView.setVisibility(View.GONE);
				}
				if (null != titleLayout) {
					titleLayout.setVisibility(View.GONE);
				}
			}
		});
	}

	// 下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		
//		if(isActionBar)
//		{
			mDialog.show();
			return;
//			popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
//		}
//		else {
//			popupWindow.showAsDropDown(
//					parent,
//					isFullScreen ? widthFullScreenPopuWindow : context
//							.getResources().getDimensionPixelSize(
//									R.dimen.popmenu_xoff),
//					// 保证尺寸是根据屏幕像素密度来的
//					isFullScreen ? heightFullScreenPopuWindow : context
//							.getResources().getDimensionPixelSize(
//									R.dimen.popmenu_yoff));
//		}
//		
		// popupWindow.showAsDropDown(parent,
		// context.getResources().getDimensionPixelSize(
		// R.dimen.popmenu_xoff),
		// // 保证尺寸是根据屏幕像素密度来的
		// context.getResources().getDimensionPixelSize(
		// R.dimen.popmenu_yoff));

		// 使其聚集
//		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
//		popupWindow.setOutsideTouchable(true);
//		// 刷新状态
//		popupWindow.update();
	}

	// 隐藏菜单
	public void dismissPopuwindow() {
		mDialog.dismiss();
		popupWindow.dismiss();
	}

	/**
	 * 
	 * ChooserDialog设置Adapter,并更新视图
	 * 
	 * @Title: setAdapter
	 * @param adapter
	 *            适配器
	 * @return void
	 * @date 2013-5-21 上午10:06:03
	 */
	public void setAdapter(ChooserAdapter adapter) {
		if (adapter != null) {
			this.adapter = adapter;
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	private OnItemSelectedListener onItemSelectedListener;

	public void setOnItemSelectedListener(
			OnItemSelectedListener onItemSelectedListener) {
		this.onItemSelectedListener = onItemSelectedListener;
	}

	public static interface OnItemSelectedListener {
		void onItemSelected(ChooserAdapter adapter, int position);

	}



	/**
	 * 
	 * 是否进行过选择动作的执行操作,主要提供给上层用于识别是否发生Action操作
	 * 
	 * @Title: isPerformChooserAction
	 * @return
	 * @return boolean
	 * @date 2013-5-21 下午4:50:11
	 */
	public boolean isPerformChooserAction() {
		return isPerformChooserAction;
	}

	public void setPerformChooserAction(boolean isPerformChooserAction) {
		this.isPerformChooserAction = isPerformChooserAction;
	}

	public boolean isShowing() {
		return null != mDialog ? mDialog.isShowing() : false;
	}
}
