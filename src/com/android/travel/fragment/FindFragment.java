package com.android.travel.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.android.travel.R;
/**
 * 发现
 * @author yangchj
 * @date 2014-10-25 下午9:48:54
 */
public class FindFragment extends SherlockFragment {

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		 setHasOptionsMenu(true);
		 getActivity().setTitle("发现");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,	ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_fragment_find,container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem settingItem=menu.add(Menu.NONE, 0, 0, "设置");
		settingItem.setIcon(R.drawable.icon_center_setting);
		settingItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case 0:
//			Intent intent=new Intent(getActivity(),SettingActivity.class);
//			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
