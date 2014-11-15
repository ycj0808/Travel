package com.android.travel.activity;

import com.android.travel.R;
import com.android.travel.db.DBHelper;
import com.android.travel.util.PreferenceConstants;
import com.android.travel.util.PreferenceUtils;
import com.ycj.android.common.utils.CryptUtils;
import com.ycj.android.common.utils.SecurityUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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
	private DBHelper helper=null;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_splash);
		mContext=this;
		helper=new DBHelper(mContext);
		boolean is_first_boot=PreferenceUtils.getPrefBoolean(mContext, PreferenceConstants.IS_First_BOOT, true);
		if(is_first_boot){
			ContentValues values=new ContentValues();
			values.put("uname", "admin");
			values.put("pwd", CryptUtils.md5("123456"));
			helper.insertUser(values);
			PreferenceUtils.setPrefBoolean(mContext, PreferenceConstants.IS_First_BOOT, false);
		}
		mHandler = new Handler();
		mHandler.postDelayed(gotoMainAct, 3000);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		helper.close();
	};
	
	//跳转到主页面
	Runnable gotoMainAct = new Runnable() {
		@Override
		public void run() {
			startActivity(new Intent(SplashActivity.this, LoginActivity.class));
			finish();
		}
	};
	
}
