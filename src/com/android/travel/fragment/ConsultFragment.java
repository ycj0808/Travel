package com.android.travel.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.android.travel.R;
import com.android.travel.activity.AddMenuActivity;
import com.android.travel.db.MenuService;

/**
 * 菜单管理
 * @author yangchj
 * @date 2014-10-25 下午9:47:15
 */
public class ConsultFragment extends SherlockFragment {
	
	private MenuService menuService;
	private Handler handler;
	
	private ListView lv_menus;
	private TextView tv_msg;//暂无菜单
	
	private List<Map<String,Object>> listMenus;
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

		@SuppressLint("InflateParams") 
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
				
			}
			return null;
		}
		
		class ViewHolder{
			TextView tv_menu_name;
			TextView tv_price;
			TextView tv_remark;
		}
	}
}
