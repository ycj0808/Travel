package com.android.travel.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnItemClickListener;
import zrc.widget.ZrcListView.OnStartListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.StateListDrawable;
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
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.android.travel.R;
import com.android.travel.activity.AddRegisterActivity;
import com.android.travel.activity.RegisterDetailActivity;
import com.android.travel.db.DBHelper;
import com.android.travel.db.RegisterService;
import com.android.travel.util.ImageUtils;
import com.android.travel.util.SerializableMap;
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
	
	private List<Map<String,Object>> listUsers=new ArrayList<Map<String,Object>>();
//	private DBHelper helper;
	
	private RegisterAdapter registerAdapter;
	
	private static int PAGE_NUM=0;
	private static final int PAGE_SIZE=25;
	
	private static int count=0;
	
	private RegisterService registerService;
	
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
//		helper=new DBHelper(getActivity());
		handler = new Handler();
		registerService=new RegisterService(getActivity());
		
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
		//加载更多
		zListView.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				loadMore();
			}
		});
		//单击
		zListView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(ZrcListView parent, View view, int position, long id) {
				Map<String,Object> map=(Map<String, Object>) parent.getItemAtPosition(position);
				SerializableMap sMap=new SerializableMap();
				sMap.setMap(map);
				Bundle bundle=new Bundle();
				bundle.putSerializable("data", sMap);
				Intent intent=new Intent(getActivity(),RegisterDetailActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
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
				convertView=LayoutInflater.from(getActivity()).inflate(R.layout.item_user, null);
				viewHolder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
				viewHolder.tv_phone=(TextView) convertView.findViewById(R.id.tv_phone);
				viewHolder.tv_datetime=(TextView) convertView.findViewById(R.id.tv_datetime);
				convertView.setTag(R.id.tag_user+position, viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag(R.id.tag_user+position);
			}
			
			if(listUsers!=null&&listUsers.size()>0){
				Map<String,Object> map=listUsers.get(position);
				viewHolder.tv_name.setText(map.get("cname").toString());
				viewHolder.tv_phone.setText(map.get("cphone").toString());
				viewHolder.tv_datetime.setText(map.get("register_time").toString());
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
		switch(curr_item){
		case 0://查询所有的客户登记
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					PAGE_NUM=0;
					listUsers.clear();
					listUsers=registerService.getRegisters(PAGE_NUM*PAGE_SIZE, PAGE_SIZE);
					if(listUsers.size()==0){
						count=0;
						ToastUtil.showShort(getActivity(), "没有查询到数据");
					}else{
						Map<String,Object> map=listUsers.get(0);
						count=Integer.valueOf(map.get("count").toString());
					}
					registerAdapter.notifyDataSetChanged();
                    zListView.setRefreshSuccess("加载成功"); // 通知加载成功
                    zListView.startLoadMore(); // 开启LoadingMore功能
				}
			},2 * 1000);
			break;
		case 1:
			if("".equals(et_search.getText().toString())){
				ToastUtil.showShort(getActivity(), "请输入查询内容");
				return;
			}
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					PAGE_NUM=0;
					listUsers.clear();
					listUsers=registerService.getRegistersByName(et_search.getText().toString());
					if(listUsers.size()==0){
						ToastUtil.showShort(getActivity(), "没有查询到数据");
					}
					registerAdapter.notifyDataSetChanged();
                    zListView.setRefreshSuccess("加载成功"); // 通知加载成功
				}
			},2 * 1000);
			break;
		case 2:
			if("".equals(et_search.getText().toString())){
				ToastUtil.showShort(getActivity(), "请输入查询内容");
				return;
			}
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					PAGE_NUM=0;
					listUsers.clear();
					listUsers=registerService.getRegistersByPhone(et_search.getText().toString());
					if(listUsers.size()==0){
						ToastUtil.showShort(getActivity(), "没有查询到数据");
					}
					registerAdapter.notifyDataSetChanged();
                    zListView.setRefreshSuccess("加载成功"); // 通知加载成功
				}
			},2 * 1000);
			break;	
		}
	}
	/**
	 * 加载更多
	 */
	private void loadMore(){
		if(curr_item==0){
			handler.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	PAGE_NUM++;
	            	int firstRes=PAGE_NUM*PAGE_SIZE;
	            	if(firstRes<count){
	            		List<Map<String,Object>> list=registerService.getRegisters(firstRes+1, PAGE_SIZE);
	            		listUsers.addAll(list);
		            	registerAdapter.notifyDataSetChanged();
		            	zListView.setLoadMoreSuccess();
	            	}else{
	            		zListView.stopLoadMore();
	            	}

	            }
	        }, 2 * 1000);
		}else{
			count=0;
			return;
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem addItem=menu.add(Menu.NONE, 0, 0, "添加");
		addItem.setIcon(R.drawable.icon_add);
		addItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case 0:
			Intent intent=new Intent(getActivity(),AddRegisterActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}









