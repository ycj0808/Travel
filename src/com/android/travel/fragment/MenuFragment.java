package com.android.travel.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.android.travel.R;
import com.android.travel.activity.AddMenuActivity;
import com.android.travel.activity.EditMenuActivity;
import com.android.travel.db.MenuService;
import com.android.travel.util.ToastUtil;

/**
 * 菜单管理
 * @author yangchj
 * @date 2014-10-25 下午9:47:15
 */
public class MenuFragment extends SherlockFragment {
	
	private MenuService menuService;
	private Handler handler;
	
	private ListView lv_menus;
	private TextView tv_msg;//暂无菜单
	
	private List<Map<String,Object>> listMenus;
	DecimalFormat format=new DecimalFormat("##0.00");
	
	private MenuAdapter adapter;
	private ProgressDialog pd; 
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle("菜单管理");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler();
		listMenus=new ArrayList<Map<String,Object>>();
		menuService=new MenuService(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,	ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_fragment_consult,container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		lv_menus=(ListView) view.findViewById(R.id.lv_menus);
		tv_msg=(TextView) view.findViewById(R.id.tv_msg);
		adapter=new MenuAdapter();
		lv_menus.setAdapter(adapter);
		setListener();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}
	
	/**
	 * 设置监听事件
	 */
	private void setListener(){
		//进入编辑页
		lv_menus.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				Map<String,Object> map=(Map<String, Object>) arg0.getItemAtPosition(position);
				if(map!=null){
					Bundle bundle=new Bundle();
					Intent intent=new Intent(getActivity(),EditMenuActivity.class);
					bundle.putInt("mid", Integer.valueOf(map.get("mid").toString()));
					bundle.putString("mname", map.get("mname").toString());
					bundle.putString("mtype", map.get("mtype").toString());
					bundle.putDouble("mprice", Double.valueOf(map.get("mprice").toString()));
					bundle.putString("mremark", map.get("mremark").toString());
					bundle.putInt("unit", Integer.valueOf(map.get("unit").toString()));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
		//长按事件
		lv_menus.setOnItemLongClickListener(new OnItemLongClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
				Map<String,Object> map=(Map<String, Object>) arg0.getItemAtPosition(position);
				if(map!=null){
					showConfirmDialog("您确认要删除该项吗?",Integer.valueOf(map.get("mid").toString()),position);
				}
				return true;
			}
		});
	}
	/**
	 * 确认菜单
	 */
	private void showConfirmDialog(String message,final int mid,final int pos){
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage(message);
		builder.setTitle("温馨提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						int res=menuService.updateMenuStatus(mid);
						if(res!=0){
							listMenus.remove(pos);
							adapter.notifyDataSetChanged();
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
	 * 刷新
	 */
	private void refresh(){
		pd=ProgressDialog.show(getActivity(), null, "加载中,请稍后……");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				listMenus=menuService.getMenus();
				if(listMenus!=null&&listMenus.size()>0){
					lv_menus.setVisibility(View.VISIBLE);
					tv_msg.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
				}else{
					lv_menus.setVisibility(View.GONE);
					tv_msg.setVisibility(View.VISIBLE);
				}
				pd.dismiss();
			}
		}, 500);
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
			Intent intent=new Intent(getActivity(),AddMenuActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 菜单适配器
	 * @author yangchj
	 */
	class MenuAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(listMenus!=null&&listMenus.size()>0){
				return listMenus.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if(listMenus!=null&&listMenus.size()>0){
				return listMenus.get(position); 
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint({ "InflateParams", "ViewTag" }) 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(convertView==null||convertView.getTag(R.id.tag_menu+position)==null){
				viewHolder=new ViewHolder();
				convertView=LayoutInflater.from(getActivity()).inflate(R.layout.item_menu, null);
				viewHolder.tv_menu_name=(TextView) convertView.findViewById(R.id.tv_menu_name);
				viewHolder.tv_price=(TextView) convertView.findViewById(R.id.tv_price);
				viewHolder.tv_remark=(TextView) convertView.findViewById(R.id.tv_remark);
				convertView.setTag(R.id.tag_menu+position, viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag(R.id.tag_menu+position);
			}
			if(listMenus!=null&&listMenus.size()>0){
				Map<String,Object> map=listMenus.get(position);
				int unit=Integer.valueOf(map.get("unit").toString());
				viewHolder.tv_menu_name.setText(map.get("mname").toString());
				switch (unit) {
				case 0:
					viewHolder.tv_price.setText(format.format(Double.valueOf(map.get("mprice").toString()))+"(/时)");
					break;
				case 1:
					viewHolder.tv_price.setText(format.format(Double.valueOf(map.get("mprice").toString()))+"(/天)");
					break;
				case 2:
					viewHolder.tv_price.setText(format.format(Double.valueOf(map.get("mprice").toString()))+"(/半天)");
					break;
				}
//				viewHolder.tv_price.setText(format.format(Double.valueOf(map.get("mprice").toString())));
				viewHolder.tv_remark.setText(map.get("mremark").toString());
			}
			return convertView;
		}
		
		class ViewHolder{
			TextView tv_menu_name;
			TextView tv_price;
			TextView tv_remark;
		}
	}
}
