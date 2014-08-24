package com.android.travel.activity;

import com.android.travel.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * 启动动画页面
 * @author yangchj
 * @date 2014年8月24日 下午8:07:04
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_splash);
	}
}
