package com.android.travel.activity;

import android.os.Bundle;
import android.view.KeyEvent;

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//屏蔽实体键的menu菜单
		if(keyCode == KeyEvent.KEYCODE_MENU){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
