package com.youku.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.youkuloginsdk.R;
import com.youku.login.base.YoukuLoginOperator;

public class TestActivity extends Activity{
	
	private Button buttonLogin = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_main);
		buttonLogin = (Button) super.findViewById(R.id.but_login);
		
		buttonLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {  
				//YoukuLoginOperator.showLoginView(TestActivity.this, LoginRegistCardViewDialogActivity.KEY_FROM, LoginRegistCardViewDialogActivity.INTENT_INTERACT_POINT);
				YoukuLoginOperator.showLoginView(TestActivity.this);
			} 
		}); 
	}
	
}
