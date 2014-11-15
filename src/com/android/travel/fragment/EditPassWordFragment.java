package com.android.travel.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.android.travel.R;
import com.android.travel.db.UserService;
import com.android.travel.util.ToastUtil;
import com.ycj.android.common.utils.CryptUtils;
/**
 * 我的
 * @author yangchj
 * @date 2014-10-25 下午9:48:10
 */
public class EditPassWordFragment extends SherlockFragment {
	
	private UserService userService;
	private EditText et_old_pwd;
	private EditText et_new_pwd;
	private Button btn_save;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	@Override
	public void onActivityCreated( Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle("修改密码");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userService=new UserService(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,	 ViewGroup container,  Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_fragment_mine,container, false);
	}
	
	@Override
	public void onViewCreated(View view,  Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		et_old_pwd=(EditText) view.findViewById(R.id.et_old_pwd);
		et_new_pwd=(EditText) view.findViewById(R.id.et_new_pwd);
		btn_save=(Button) view.findViewById(R.id.btn_save);
		setListener();
	}
	/**
	 * 设置监听事件
	 */
	private void setListener(){
		//保存
		btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String old_pwd=et_old_pwd.getText().toString();
				String new_pwd=et_new_pwd.getText().toString();
				if(TextUtils.isEmpty(old_pwd)){
					ToastUtil.showShort(getActivity(), "请输入原密码!");
					return;
				}
				
				if(TextUtils.isEmpty(new_pwd)){
					ToastUtil.showShort(getActivity(), "请输入新密码!");
					return;
				}
				
				boolean isRight=userService.login("admin", CryptUtils.md5(old_pwd));
				if(!isRight){
					ToastUtil.showShort(getActivity(), "原密码不正确");
					return;
				}
				int res=userService.updateUser(CryptUtils.md5(new_pwd), 1);
				if(res>0){
					ToastUtil.showShort(getActivity(), "修改密码成功!");
				}
			}
		});
	}
}
