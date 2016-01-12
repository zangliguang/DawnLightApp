package com.youku.login.Zxing.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.youkuloginsdk.R;
import com.youku.login.widget.YoukuLoading;


public class CustomDialog extends Dialog implements View.OnClickListener {

	private TextView soku_dialog_txt = null;
	private View layout_soku_dialog_cancel = null;
	private View layout_soku_dialog_goplay = null;
	private View.OnClickListener confirmBtnListener;
	private View.OnClickListener cancelBtnListener;
	private String showText = null;
	private Context context;

	public CustomDialog(Context context) {
		super(context, R.style.SoKuDialog);
		this.context = context;
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public static void showSokuDialog(Context mContext, String showText, View.OnClickListener confirmBtnListener, View.OnClickListener cancelBtnListener) {
		CustomDialog mSoKuDialog = new CustomDialog(mContext);
		mSoKuDialog.setShowText(showText);
		mSoKuDialog.setConfirmBtnListener(confirmBtnListener);
		mSoKuDialog.setCancelBtnListener(cancelBtnListener);
		mSoKuDialog.show();
	}

	private void setCancelBtnListener(
			android.view.View.OnClickListener cancelBtnListener) {
		// TODO Auto-generated method stub
		this.cancelBtnListener = cancelBtnListener;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_dialog_view);
		initView();
	}

	private void initView() {
		soku_dialog_txt = (TextView) findViewById(R.id.soku_dialog_txt);
		layout_soku_dialog_cancel = findViewById(R.id.layout_soku_dialog_cancel);
		layout_soku_dialog_goplay = findViewById(R.id.layout_soku_dialog_goplay);
		layout_soku_dialog_cancel.setOnClickListener(this);
		layout_soku_dialog_goplay.setOnClickListener(this);
		if(!TextUtils.isEmpty(showText)) {
			soku_dialog_txt.setText(showText);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.layout_soku_dialog_cancel){
			dismiss();
			if (cancelBtnListener != null) {
				cancelBtnListener.onClick(v);
			}
		}else if(id == R.id.layout_soku_dialog_goplay){
			YoukuLoading.show(context);
			dismiss();
			if(confirmBtnListener != null) {
				confirmBtnListener.onClick(v);
			}
		}
		/*
		switch (v.getId()) {
		case R.id.layout_soku_dialog_cancel:
			dismiss();
			if (cancelBtnListener != null) {
				cancelBtnListener.onClick(v);
			}
			break;
		case R.id.layout_soku_dialog_goplay:
			YoukuLoading.show(context);
			dismiss();
			if(confirmBtnListener != null) {
				confirmBtnListener.onClick(v);
			}
			break;
		default:
			break;
		}*/
	}
	
	public void setShowText(String showText) {
		this.showText = showText;
		if(!TextUtils.isEmpty(showText) && soku_dialog_txt != null) {
			soku_dialog_txt.setText(showText);
		}
	}
	
	public void setConfirmBtnListener(View.OnClickListener confirmBtnListener) {
		this.confirmBtnListener = confirmBtnListener;
	}
}
