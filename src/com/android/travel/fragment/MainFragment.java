package com.android.travel.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.android.travel.R;
/**
 * 主页片段
 * @author yangchj
 * @date 2014-10-25 下午9:45:18
 */
public class MainFragment extends SherlockFragment {

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onActivityCreated( Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().setTitle(R.string.app_name);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,	 ViewGroup container,  Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_fragment_main,container, false);
	}
	
	@Override
	public void onViewCreated(View view,  Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
}
