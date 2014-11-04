package com.android.travel.activity;

import android.os.Bundle;
import android.view.KeyEvent;

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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//屏蔽实体键的menu菜单
		if(keyCode == KeyEvent.KEYCODE_MENU){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
