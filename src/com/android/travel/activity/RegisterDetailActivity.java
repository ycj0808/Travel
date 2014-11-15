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
import com.android.travel.ui.ListViewForScrollView;
import com.android.travel.util.ImageUtils;
import com.android.travel.util.LogUtil;
import com.android.travel.util.SerializableMap;
import com.android.travel.util.ToastUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
	private TextView tv_item_num;
	private TextView tv_item_price;
	
	private ListViewForScrollView lv_register_detail;
	private Button btn_add;
	
	private Button btn_check;
	
	private RelativeLayout layout_tip;
	
	private List<Map<String,Object>> listData;
	private static int rid=-1;
	DecimalFormat format=new DecimalFormat("##");
	private Handler handler;
	
	private RegisterDetailAdapter adapter;
	private RegisterService registerService;
	
	private double total=0;
	
	private int status=1;
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
		
		tv_item_num=(TextView) findViewById(R.id.item_num);
		tv_item_price=(TextView) findViewById(R.id.item_price);
		
		lv_register_detail=(ListViewForScrollView) findViewById(R.id.lv_register_detail);
		btn_add=(Button) findViewById(R.id.btn_add);
		
		btn_check=(Button) findViewById(R.id.btn_check);
		layout_tip=(RelativeLayout) findViewById(R.id.layout_tip);
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
			
			status=bundle.getInt("status");
			if(status==2){
				btn_check.setVisibility(View.GONE);
				layout_tip.setVisibility(View.GONE);
			}
			
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
		btn_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpToRegisterDetail();
			}
		});
		//结账
		btn_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listData==null||listData.size()==0){
					ToastUtil.showShort(mContext, "还没有添加任何项目,无法结账!");
					return;
				}
				checkConfirmDialog(rid);
			}
		});
		//长按删除
		lv_register_detail.setOnItemLongClickListener(new OnItemLongClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(status==2){
					ToastUtil.showShort(mContext, "历史订单明细不可删除!");
				}else{
					Map<String,Object> map=(Map<String, Object>) arg0.getItemAtPosition(arg2);
					if(map!=null&&!map.isEmpty()){
						int rdid=Integer.valueOf(map.get("rdid").toString());
						confirmDeleteDialog(rdid, arg2);
					}
				}
				return true;
			}
		});
	}
	
	private void jumpToRegisterDetail(){
		Bundle bundle=new Bundle();
		bundle.putInt("rid", rid);
		Intent intent=new Intent(mContext,AddRegisterDetailActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
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
					if(status!=2){
						btn_add.setVisibility(View.VISIBLE);
					}else{
						btn_add.setVisibility(View.GONE);
					}
					
				}else{
					lv_register_detail.setVisibility(View.VISIBLE);
					btn_add.setVisibility(View.GONE);
				}
				updateTotalAmount();
			}
		}, 500);
	}
	/**
	 * 更新总金额
	 */
	private void updateTotalAmount(){
		total=0;
		for(int i=0;i<listData.size();i++){
			Map<String,Object> map=listData.get(i);
			double mPrice=Double.valueOf(map.get("mprice").toString());
			int num=Integer.valueOf(map.get("person_num").toString());
			int time=Integer.valueOf(map.get("dayorhour").toString());
			total+=mPrice*num*time;
		}
		tv_item_price.setText(format.format(total));
		tv_item_num.setText(String.valueOf(listData.size()));
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(status!=2){
			MenuItem addItem=menu.add(Menu.NONE, 0, 0, "添加");
			addItem.setIcon(R.drawable.icon_add);
			addItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			menu.add(Menu.NONE, 1, 1, "删除").setIcon(R.drawable.clear_icon).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home://返回
			finish();
			break;
		case 0:
			if(status!=2){
				jumpToRegisterDetail();
			}
			break;
		case 1:
			if(status!=2){
				showConfirmDialog("您确认要删除该条登记纪录吗？", rid);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	/**
	 * 显示确认框
	 * @param message
	 * @param mid
	 * @param pos
	 */
	private void showConfirmDialog(String message,final int rid){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage(message);
		builder.setTitle("温馨提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						int res=registerService.delRegister(rid);
						if(res!=0){
							ToastUtil.showShort(mContext, "删除成功!");
							finish();
						}else{
							ToastUtil.showShort(mContext, "删除失败!");
						}
					}
				}, 500);
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
	/**
	 * 结账确认框
	 * @param rid
	 */
	private void checkConfirmDialog(final int rid){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("您确认要结账吗?");
		builder.setTitle("温馨提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						int res=registerService.updateRegister(rid);
						if(res!=0){
							ToastUtil.showShort(mContext, "结账成功!");
							finish();
						}else{
							ToastUtil.showShort(mContext, "结账失败!");
						}
					}
				}, 500);
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
	/**
	 * 确认要删除单项纪录
	 * @param rdid
	 */
	private void confirmDeleteDialog(final int rdid,final int pos){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("您确认要删除该单项纪录吗?");
		builder.setTitle("温馨提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						int res=registerService.deleteRegisterDetail(rdid);
						if(res!=0){
							ToastUtil.showShort(mContext, "删除成功!");
							listData.remove(pos);
							updateTotalAmount();
						}else{
							ToastUtil.showShort(mContext, "删除失败!");
						}
					}
				}, 500);
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
				convertView=LayoutInflater.from(mContext).inflate(R.layout.item_register_detail, null);
				viewHolder.tv_menu_name=(TextView) convertView.findViewById(R.id.tv_menu_name);
				viewHolder.tv_menu_price=(TextView) convertView.findViewById(R.id.tv_menu_price);
				viewHolder.tv_person_num=(TextView) convertView.findViewById(R.id.tv_person_num);
				viewHolder.tv_menu_remark=(TextView) convertView.findViewById(R.id.tv_menu_remark);
				viewHolder.tv_time_tip=(TextView) convertView.findViewById(R.id.tv_time_tip);
				viewHolder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
				viewHolder.tv_total_amount_tip=(TextView) convertView.findViewById(R.id.tv_total_amount_tip);
				viewHolder.tv_total_amount=(TextView) convertView.findViewById(R.id.tv_total_amount);
				convertView.setTag(R.id.tag_register+position, viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag(R.id.tag_register+position);
			}
			if(listData!=null&&listData.size()>0){
				Map<String,Object> map=listData.get(position);
				double mprice=Double.valueOf(map.get("mprice").toString());
				int num=Integer.valueOf(map.get("person_num").toString());
				int time=Integer.valueOf(map.get("dayorhour").toString());
				double total=mprice*num*time;
				viewHolder.tv_menu_name.setText(map.get("mname").toString());
				viewHolder.tv_menu_price.setText(String.valueOf(mprice));
				viewHolder.tv_menu_remark.setText(map.get("mremark").toString());
				viewHolder.tv_person_num.setText(String.valueOf(num));
				viewHolder.tv_time.setText(String.valueOf(time));
				viewHolder.tv_total_amount.setText(format.format(total));
				int unit=Integer.valueOf(map.get("unit").toString());
				switch (unit) {
				case 0:
					viewHolder.tv_time_tip.setText("参与时长:(时)");
					break;
				case 1:
					viewHolder.tv_time_tip.setText("参与时长:(天)");
					break;
				case 2:
					viewHolder.tv_time_tip.setText("参与时长:(半天)");
					break;	
				}
			}
			return convertView;
		}
		
		class ViewHolder{
			TextView tv_menu_name;
			TextView tv_menu_price;
			TextView tv_person_num;
			TextView tv_menu_remark;
			TextView tv_time_tip;
			TextView tv_time;
			TextView tv_total_amount_tip;
			TextView tv_total_amount;
		}
	}
}
