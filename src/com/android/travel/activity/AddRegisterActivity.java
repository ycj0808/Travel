package com.android.travel.activity;

import java.util.List;
import java.util.Map;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.android.travel.R;
import com.android.travel.db.CustomerService;
import com.android.travel.db.RegisterService;
import com.android.travel.util.ImageUtils;
import com.android.travel.util.LogUtil;
import com.android.travel.util.ToastUtil;
import com.ycj.android.common.utils.RegexUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 添加注册
 * @author yangchj
 */
public class AddRegisterActivity extends BaseActivity {

	private ActionBar actionBar;
	private Context mContext;
	
	private EditText et_name;
	private EditText et_phone;
	private Button btn_save;
	private Handler handler;
	private CustomerService customerService;
	private RegisterService registerService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_add_register);
		initActionBar();
		initView();
		setListener();
	}
	
	private void initActionBar(){
		actionBar=getCustomActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);// logo不显示
		actionBar.setTitle("客户登记");
	}
	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void initView(){
		mContext=this;
		handler=new Handler();
		customerService=new CustomerService(mContext);
		registerService=new RegisterService(mContext);
		et_name=(EditText) findViewById(R.id.et_name);
		et_phone=(EditText) findViewById(R.id.et_phone);
		btn_save=(Button) findViewById(R.id.btn_save);
	}
	
	private void setListener(){
		//保存
		btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String name=et_name.getText().toString();
			    final String phone=et_phone.getText().toString();			
				if(TextUtils.isEmpty(name)){
					ToastUtil.showShort(mContext, "请填写客户姓名!");
					return;
				}
				
				if(TextUtils.isEmpty(phone)){
					ToastUtil.showShort(mContext, "请填写手机号!");
					return;
				}
				if(!RegexUtils.checkMobile(phone)){
					ToastUtil.showShort(mContext, "手机号格式不正确");
					return;
				}
				
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Map<String,Object> map=customerService.getCustomerByPhone(phone);
						if(map!=null&&map.containsKey("cid")){
//							ToastUtil.showShort(mContext, "该手机号已经被用户: "+map.get("cname").toString()+" 登记");
							if(!name.equals(map.get("cname").toString())){
								LogUtil.i("该手机号已经被用户: "+map.get("cname").toString()+" 登记过");
								ToastUtil.showShort(mContext, "该手机号已经被用户: "+map.get("cname").toString()+" 登记");
								return;
							}else{
								List<Map<String,Object>> list=registerService.getRegistersByPhone(phone,1);
								if(list!=null&&list.size()>0){
									Map<String,Object> registerMap=list.get(0);
									String time=registerMap.get("register_time").toString();
									showAlertDialog("该用户已在"+time+"登记过了,还尚未结账,您确定再次登记吗？", Integer.valueOf(map.get("cid").toString()));
								}else{
									registerService.addRegister(Integer.valueOf(map.get("cid").toString()));
									ToastUtil.showShort(mContext,"保存成功!");
									finish();
								}
							}
						}else{
							//注册客户信息
							int cid=customerService.addCustomer(name, phone);
							if(cid!=-1){
								//客户登记
								registerService.addRegister(cid);
								ToastUtil.showShort(mContext,"保存成功!");
								finish();
							}else{
								ToastUtil.showShort(mContext,"登记失败!");
							}
						}
					}
				}, 500);
			}
		});
	}
	/**
	 * 警告消息
	 * @param message
	 */
	private void showAlertDialog(String message,final int cid){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage(message);
		builder.setTitle("温馨提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//客户登记（客户已注册过）
				registerService.addRegister(cid);
				ToastUtil.showShort(mContext,"保存成功!");
				finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home://返回
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
