package com.android.travel.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

/**
 *  基础类
 * @author yangchj
 * @date 2014年11月3日 下午9:04:29
 */
public class BaseActivity extends SherlockActivity {

	private ActionBar actionBar;
	
	public ActionBar getCustomActionBar() {
		return actionBar;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar=getSupportActionBar();
	}
}
