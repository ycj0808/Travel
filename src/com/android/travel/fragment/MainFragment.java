package com.android.travel.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnStartListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.android.travel.R;
import com.android.travel.db.DBHelper;
import com.android.travel.util.ImageUtils;
import com.android.travel.util.TimeUtil;
import com.android.travel.util.ToastUtil;
/**
 * 主页片段
 * @author yangchj
 * @date 2014-10-25 下午9:45:18
 */
public class MainFragment extends SherlockFragment {

	private Button btn_search;
	private Spinner spinner;
	private EditText et_search;
	private ZrcListView zListView;
	
	private static final String[] m={"全部","姓名","手机号"};
	private ArrayAdapter<String> adapter;
	private static int curr_item=0;
	
	private List<Map<String,String>> listUsers=new ArrayList<Map<String,String>>();
	private DBHelper helper;
	
	private RegisterAdapter registerAdapter;
	
	private Handler handler;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onActivityCreated( Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle("用户登记列表");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		helper=new DBHelper(getActivity());
		handler = new Handler();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,	 ViewGroup container,  Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_fragment_main,container, false);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onViewCreated(View view,  Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		btn_search=(Button) view.findViewById(R.id.btn_search);
		spinner=(Spinner) view.findViewById(R.id.spinner01);
		et_search=(EditText) view.findViewById(R.id.et_search);
		zListView=(ZrcListView) view.findViewById(R.id.zListView);
		
        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(getActivity());
        header.setTextColor(0xff0066aa);
        header.setCircleColor(0xff33bbee);
        zListView.setHeadable(header);
        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(getActivity());
        footer.setCircleColor(0xff33bbee);
        zListView.setFootable(footer);

        // 设置列表项出现动画（可选）
        zListView.setItemAnimForTopIn(R.anim.topitem_in);
        zListView.setItemAnimForBottomIn(R.anim.bottomitem_in);
        
		adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,m);
		 //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        
        registerAdapter=new RegisterAdapter();
        zListView.setAdapter(registerAdapter);
        zListView.refresh();
        setListener();
	}
	/**
	 *  设置监听事件
	 */
	private void setListener(){
		//选择查询类型
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				curr_item=arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		//查询
		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refresh();
			}
		});
		
        // 下拉刷新事件回调（可选）
		zListView.setOnRefreshStartListener(new OnStartListener() {
            @Override
            public void onStart() {
                refresh();
            }
        });
	}
	
	
	class RegisterAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(listUsers!=null&&listUsers.size()>0){
				return listUsers.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if(listUsers!=null&&listUsers.size()>0){
				return listUsers.get(position);
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
			if(convertView==null||convertView.getTag(R.id.tag_user+position)==null){
				viewHolder=new ViewHolder();
				viewHolder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
				viewHolder.tv_phone=(TextView) convertView.findViewById(R.id.tv_phone);
				viewHolder.tv_datetime=(TextView) convertView.findViewById(R.id.tv_datetime);
				convertView.setTag(R.id.tag_user+position, viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag(R.id.tag_user+position);
			}
			
			if(listUsers!=null&&listUsers.size()>0){
				Map<String,String> map=listUsers.get(position);
				viewHolder.tv_name.setText(map.get("uname"));
				viewHolder.tv_phone.setText(map.get("phone"));
				viewHolder.tv_datetime.setText(TimeUtil.getFormatTime(Long.valueOf(map.get("register_time"))));
			}
			return convertView;
		}
		
		class ViewHolder{
			TextView tv_name;
			TextView tv_phone;
			TextView tv_datetime;
		}
	}
	/**
	 *  
	 */
	private void refresh(){
		switch (curr_item) {
		case 0:
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Cursor cursor=helper.qryCustomerRegisters("", null);
					listUsers.clear();
					while(cursor.moveToNext()){
						Map<String,String> map=new HashMap<String,String>();
						map.put("rid", String.valueOf(cursor.getInt(0)));
						map.put("cid", String.valueOf(cursor.getInt(1)));
						map.put("uname", cursor.getString(2));
						map.put("phone", cursor.getString(3));
						map.put("register_time", String.valueOf(cursor.getLong(4)));
						listUsers.add(map);
					}
					if(listUsers.size()==0){
						ToastUtil.showShort(getActivity(), "没有查询到数据");
					}
					registerAdapter.notifyDataSetChanged();
                    zListView.setRefreshSuccess("加载成功"); // 通知加载成功
				}
			},2 * 1000);
			break;
		case 1:
			if("".equals(et_search.getText().toString())){
				ToastUtil.showShort(getActivity(), "请输入查询内容");
				return;
			}else{
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Cursor cursor=helper.qryCustomerRegisters(" and c.cname=?", new String []{et_search.getText().toString()});
						listUsers.clear();
						while(cursor.moveToNext()){
							Map<String,String> map=new HashMap<String,String>();
							map.put("rid", String.valueOf(cursor.getInt(0)));
							map.put("cid", String.valueOf(cursor.getInt(1)));
							map.put("uname", cursor.getString(2));
							map.put("phone", cursor.getString(3));
							map.put("register_time", String.valueOf(cursor.getLong(4)));
							listUsers.add(map);
						}
						if(listUsers.size()==0){
							ToastUtil.showShort(getActivity(), "没有查询到数据");
						}
						registerAdapter.notifyDataSetChanged();
	                    zListView.setRefreshSuccess("加载成功"); // 通知加载成功
					}
				},2 * 1000);
			}
			break;
		case 2:
			if("".equals(et_search.getText().toString())){
				ToastUtil.showShort(getActivity(), "请输入查询内容");
				return;
			}else{
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Cursor cursor=helper.qryCustomerRegisters("", new String []{et_search.getText().toString()});
						listUsers.clear();
						while(cursor.moveToNext()){
							Map<String,String> map=new HashMap<String,String>();
							map.put("rid", String.valueOf(cursor.getInt(0)));
							map.put("cid", String.valueOf(cursor.getInt(1)));
							map.put("uname", cursor.getString(2));
							map.put("phone", cursor.getString(3));
							map.put("register_time", String.valueOf(cursor.getLong(4)));
							listUsers.add(map);
						}
						if(listUsers.size()==0){
							ToastUtil.showShort(getActivity(), "没有查询到数据");
						}
						registerAdapter.notifyDataSetChanged();
	                    zListView.setRefreshSuccess("加载成功"); // 通知加载成功
					}
				},2 * 1000);
			}
			break;					
		}
	}
}









