package com.android.travel.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.travel.R;
import com.android.travel.db.RegisterService;
import com.android.travel.util.ImageUtils;
import com.android.travel.util.LogUtil;
import com.android.travel.util.SerializableMap;
import com.android.travel.util.ToastUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 登记详情
 * @author yangchj
 */
public class RegisterDetailActivity extends BaseActivity {

	private ActionBar actionBar;
	private Context mContext;
	
	private TextView tv_name;
	private TextView tv_phone;
	private TextView tv_datetime;
	
	private ListView lv_register_detail;
	private Button btn_add;
	
	private List<Map<String,Object>> listData;
	private static int rid=-1;
	DecimalFormat format=new DecimalFormat("##");
	private Handler handler;
	
	private RegisterDetailAdapter adapter;
	private RegisterService registerService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_register_detail);
		initActionBar();
		initView();
		initData();
		setListener();
	}
	
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar(){
		actionBar=getCustomActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);	
		actionBar.setTitle("客户登记详情");
		}
	/**
	 * 初始化元素控件
	 */
	private void initView(){
		mContext=this;
		handler=new Handler();
		registerService=new RegisterService(mContext);
		listData=new ArrayList<Map<String,Object>>();
		tv_name=(TextView) findViewById(R.id.tv_name);
		tv_phone=(TextView) findViewById(R.id.tv_phone);
		tv_datetime=(TextView) findViewById(R.id.tv_datetime);
		lv_register_detail=(ListView) findViewById(R.id.lv_register_detail);
		btn_add=(Button) findViewById(R.id.btn_add);
		adapter=new RegisterDetailAdapter();
		lv_register_detail.setAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getData();
	}
	/**
	 *  初始化数据
	 */
	private void initData(){
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null&&bundle.containsKey("data")){
			SerializableMap sMap=(SerializableMap) bundle.getSerializable("data");
			Map<String,Object> map=sMap.getMap();
			tv_name.setText(map.get("cname").toString());
			tv_phone.setText(map.get("cphone").toString());
			tv_datetime.setText(map.get("register_time").toString());
			rid=Integer.valueOf(map.get("rid").toString());
		}
	}
	/**
	 * 设置监听事件
	 */
	private void setListener(){
		
	}
	
	/**
	 * 获取数据
	 */
	private void getData(){
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				listData.clear();
				listData=registerService.getRegisterDetails(rid);
				if(listData.size()==0){
					LogUtil.i("没有数据!");
					lv_register_detail.setVisibility(View.GONE);
					btn_add.setVisibility(View.VISIBLE);
				}else{
					lv_register_detail.setVisibility(View.VISIBLE);
					btn_add.setVisibility(View.GONE);
				}
				adapter.notifyDataSetChanged();
			}
		}, 2000);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem addItem=menu.add(Menu.NONE, 0, 0, "添加");
		addItem.setIcon(R.drawable.icon_add);
		addItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home://返回
			finish();
			break;
		case 0:
			ToastUtil.showShort(mContext, "添加");
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class RegisterDetailAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(listData!=null&&listData.size()>0){
				return listData.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if(listData!=null&&listData.size()>0){
				return listData.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ViewTag") 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(convertView==null||convertView.getTag(R.id.tag_register+position)==null){
				viewHolder=new ViewHolder();
				viewHolder.tv_menu_name=(TextView) convertView.findViewById(R.id.tv_menu_name);
				viewHolder.tv_menu_price=(TextView) convertView.findViewById(R.id.tv_menu_price);
				viewHolder.tv_person_num=(TextView) convertView.findViewById(R.id.tv_person_num);
				viewHolder.tv_menu_remark=(TextView) convertView.findViewById(R.id.tv_menu_remark);
				convertView.setTag(R.id.tag_register+position, viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag(R.id.tag_register+position);
			}
			if(listData!=null&&listData.size()>0){
				Map<String,Object> map=listData.get(position);
				viewHolder.tv_menu_name.setText(map.get("mname").toString());
				viewHolder.tv_menu_price.setText(map.get("mprice").toString());
				viewHolder.tv_menu_remark.setText(map.get("mremark").toString());
				viewHolder.tv_person_num.setText(map.get("person_num").toString());
			}
			return convertView;
		}
		
		class ViewHolder{
			TextView tv_menu_name;
			TextView tv_menu_price;
			TextView tv_person_num;
			TextView tv_menu_remark;
		}
	}
}
