package com.android.travel.activity;

import com.android.travel.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 启动动画页面
 * @author yangchj
 * @date 2014年8月24日 下午8:07:04
 */
public class SplashActivity extends Activity {
	private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_splash);
		mHandler = new Handler();
		mHandler.postDelayed(gotoMainAct, 3000);
	}
	
	//跳转到主页面
	Runnable gotoMainAct = new Runnable() {
		@Override
		public void run() {
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			finish();
		}
	};
}
