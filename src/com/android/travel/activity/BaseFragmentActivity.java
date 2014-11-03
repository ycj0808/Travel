package com.android.travel.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
/**
 * 基础类
 * @author yangchj
 * @date 2014年11月3日 下午9:05:14
 */
public class BaseFragmentActivity extends SherlockFragmentActivity {

	private ActionBar actionBar;
	
	public ActionBar getCustomActionBar() {
		return actionBar;
	}
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		actionBar=getSupportActionBar();
	}

	
}
