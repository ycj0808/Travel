package com.android.travel.activity;

import java.util.List;
import java.util.Map;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.android.travel.R;
import com.android.travel.db.MenuService;
import com.android.travel.db.RegisterService;
import com.android.travel.util.ToastUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 添加登记纪录
 * @author yangchj
 *
 */
public class AddRegisterDetailActivity extends BaseActivity {

	private ActionBar actionBar;
	private Context mContext;
	private Handler handler;
	
	private Spinner spinner;
	private EditText et_person_num;
	private Button btn_save;
	
	private MenuService menuService;
	private RegisterService registerService;
	private List<Map<String,Object>> listMenus;
	String  items[];
	private Map<String,Object> menuMap;
	private ArrayAdapter<String> adapter;
	private int cur_item=0;
	private int rid;
	
	private String[] unit_type;
	
	private TextView tv_time_tip;
	private EditText et_time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_add_register_detail);
		initActionBar();
		initView();
		setListener();
	}
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar(){
		actionBar=getCustomActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("登记项目");
	}
	
	private void initView(){
		mContext=this;
		handler=new Handler();
		unit_type=getResources().getStringArray(R.array.unit_type);
		menuService=new MenuService(mContext);
		registerService=new RegisterService(mContext);
		spinner=(Spinner) findViewById(R.id.spinner_item_name);
		et_person_num=(EditText) findViewById(R.id.et_person_num);
		btn_save=(Button) findViewById(R.id.btn_save);
		
		tv_time_tip=(TextView) findViewById(R.id.tv_time_tip);
		et_time=(EditText) findViewById(R.id.et_time);
		
		Bundle bundle=getIntent().getExtras();
		rid=bundle.getInt("rid");
		loadData();
	}
	/**
	 *加载数据
	 */
	private void loadData(){
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				listMenus=menuService.getMenus();
				if(listMenus!=null&&listMenus.size()>0){
					items=new String[listMenus.size()];
					for(int i=0;i<listMenus.size();i++){
						Map<String,Object> map=listMenus.get(i);
						items[i]=map.get("mname").toString();
					}
					adapter=new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, items);
					 //设置下拉列表的风格
			        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner.setAdapter(adapter);
					updateTimeTip();
				}else{
					showAlertDialog();
				}
			}
		}, 500);
	}
	
	private void updateTimeTip(){
		if(listMenus!=null&&listMenus.size()>cur_item){
			Map<String, Object> map=listMenus.get(cur_item);
			int unit=Integer.valueOf(map.get("unit").toString());
			switch (unit) {
			case 0:
				tv_time_tip.setText("参与时长(时)");
				break;
			case 1:
				tv_time_tip.setText("参与时长(天)");
				break;
			case 2:
				tv_time_tip.setText("参与时长(半天)");
				break;	
			}
		}
	}
	/**
	 * 设置监听
	 */
	private void setListener(){
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				cur_item=arg2;
				updateTimeTip();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		//保存
		btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String person_num=et_person_num.getText().toString();
				final String time=et_time.getText().toString();
				if(TextUtils.isEmpty(person_num)){
					ToastUtil.showShort(mContext, "请输入参与人数!");
					return;
				}
				
				if(listMenus==null||listMenus.size()==0){
					showAlertDialog();
					return;
				}
				
				if(TextUtils.isEmpty(time)){
					ToastUtil.showShort(mContext, "请输入参与时长!");
					return;
				}
				
				//保存
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Map<String,Object> map=listMenus.get(cur_item);
						long res=registerService.addRegisterDetail(rid, Integer.valueOf(map.get("mid").toString()), Integer.valueOf(person_num),Integer.valueOf(time));
						if(res!=-1){
							ToastUtil.showShort(mContext, "添加成功!");
							finish();
						}else{
							ToastUtil.showShort(mContext, "添加失败!");
						}
					}
				}, 500);
			}
		});
	}
	/**
	 * 警告框
	 */
	private void showAlertDialog(){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("您尚未创建项目菜单,请到菜单管理中添加菜单项!");
		builder.setTitle("温馨提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.create().show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case android.R.id.home://返回
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
