package com.android.travel.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.android.travel.R;
import com.android.travel.db.MenuService;
import com.android.travel.util.CommonUtil;
import com.android.travel.util.ToastUtil;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
/**
 * 添加菜单
 * @author yangchj
 */
public class AddMenuActivity extends BaseActivity {

	private ActionBar actionBar;
	private Context mContext;
	private Handler handler;
	
	private EditText et_menu_name;
	private EditText et_menu_price;
	private EditText et_menu_remark;
	private Button btn_save;
	
	private MenuService menuService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_add_menu);
		initActionBar();
		initView();
		setListener();
	}
	
	private void initActionBar(){
		actionBar=getCustomActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("添加管理");
	}
	
	private void initView(){
		mContext=this;
		handler=new Handler();
		menuService=new MenuService(mContext);
		et_menu_name=(EditText) findViewById(R.id.et_menu_name);
		et_menu_price=(EditText) findViewById(R.id.et_menu_price);
		et_menu_remark=(EditText) findViewById(R.id.et_menu_remark);
		btn_save=(Button) findViewById(R.id.btn_save);
	}
	/**
	 * 监听事件
	 */
	private void setListener(){
		//保存
		btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String menu_name=et_menu_name.getText().toString();
				final String menu_price=et_menu_price.getText().toString();
				final String menu_remark=et_menu_remark.getText().toString();
				if(TextUtils.isEmpty(menu_name)){
					ToastUtil.showShort(mContext, "请填写菜单名称");
					return;
				}
				
				if(TextUtils.isEmpty(menu_price)){
					ToastUtil.showShort(mContext, "请填写菜单单价");
					return;
				}
				//添加菜单
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						long res=menuService.addMenu(menu_name, CommonUtil.getGUID(), Double.valueOf(menu_price), menu_remark);
						if(res!=-1){
							ToastUtil.showShort(mContext, "添加菜单成功！");
							clearData();
						}else{
							ToastUtil.showShort(mContext, "添加菜单失败！");
						}
					}
				}, 2000);
			}
		});
	}
	/**
	 * 清空数据　
	 */
	private void clearData(){
		et_menu_name.setText("");
		et_menu_price.setText("");
		et_menu_remark.setText("");
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
