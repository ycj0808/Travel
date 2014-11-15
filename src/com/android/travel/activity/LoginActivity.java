package com.android.travel.activity;

import com.actionbarsherlock.app.ActionBar;
import com.android.travel.R;
import com.android.travel.db.DBHelper;
import com.android.travel.db.UserService;
import com.android.travel.util.ImageUtils;
import com.android.travel.util.ToastUtil;
import com.ycj.android.common.utils.CryptUtils;
import com.ycj.android.ui.utils.ToastUtils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 登陆页面
 * @author yangchj
 * @date 2014年11月3日 下午9:10:36
 */
public class LoginActivity extends BaseActivity {

	private ActionBar actionBar;
	private EditText et_account;
	private EditText et_pwd;
	private Button btn_login;
	
	private Context mContext;
	private UserService userService;
//	private DBHelper helper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_login);
		initActionBar();
		initView();
		setListener();
	}
	
	private void initActionBar(){
		actionBar=getCustomActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("登陆");
	}
	
	@SuppressWarnings("deprecation")
	private void initView(){
		mContext=this;
		et_account=(EditText) findViewById(R.id.et_account);
		et_pwd=(EditText) findViewById(R.id.et_pwd);
		btn_login=(Button) findViewById(R.id.btn_login);
//		Resources rs=getResources();
//		StateListDrawable drawable=ImageUtils.newSelector(mContext, R.drawable.bg_button, rs.getColor(R.color.base_blue), rs.getColor(R.color.blue_pressed));
//		btn_login.setBackgroundDrawable(drawable);
		userService=new UserService(mContext);
//		helper=new DBHelper(mContext);
	}
	
	private void setListener(){
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String pwd=et_pwd.getText().toString();
				if("".equals(pwd)){
					ToastUtil.showShort(mContext, "密码不能为空");
					return;
				}
				boolean is_login=userService.login("admin", CryptUtils.md5(pwd));
				if(!is_login){
					ToastUtil.showShort(mContext, "密码错误!");
				}else{
					Intent intent=new Intent(mContext,MainActivity.class);
					startActivity(intent);
					finish();
				}
//				Cursor cursor=helper.queryUserByAccountAndName("admin", CryptUtils.md5(pwd));
//				if(cursor.moveToNext()){
//					Intent intent=new Intent(mContext,MainActivity.class);
//					startActivity(intent);
//					finish();
//				}else{
//					ToastUtil.showShort(mContext, "密码错误!");
//				}
			}
		});
	}
}
