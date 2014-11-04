package com.android.travel.activity;

import com.actionbarsherlock.app.ActionBar;
import com.android.travel.R;
import com.android.travel.fragment.ConsultFragment;
import com.android.travel.fragment.FindFragment;
import com.android.travel.fragment.MainFragment;
import com.android.travel.fragment.MineFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
/**
 * 主页面
 * @author yangchj
 */
public class MainActivity extends BaseFragmentActivity {

	public ActionBar actionBar;
	public static FragmentManager fMgr;
	private MainFragment mainFragment;
	private ConsultFragment consultFragment;
	private MineFragment mineFragment;
	private FindFragment findFragment;

	private RadioGroup rg;
	private RadioButton[] rb = new RadioButton[4];
	private FrameLayout tabcontent;
	private int curr_item = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_main);
		if (savedInstanceState != null) {
			curr_item = savedInstanceState.getInt("curr_item");
		}
		initActionBar();
		initView();
		initFragment();
		setListener();
	}
	
	/**
	 * 界面销毁之前保存数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (outState != null) {
			outState.putInt("curr_item", curr_item);
		}
	}

	/**
	 * 执行于onStart()之后，回复之前保存过的数据
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		curr_item = savedInstanceState.getInt("curr_item");
	}

	/**
	 * 初始化antionBar
	 * 
	 * @author yangchj
	 * @date 2014-10-25 下午9:12:31
	 */
	private void initActionBar() {
		actionBar = getCustomActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);// logo不显示
	}

	/**
	 * 初始化元素控件
	 * 
	 * @author yangchj
	 * @date 2014-10-26 上午1:57:33
	 */
	private void initView() {
		tabcontent = (FrameLayout) findViewById(R.id.tabcontent);
		rg = (RadioGroup) findViewById(R.id.main_tab);
		rb[0] = (RadioButton) findViewById(R.id.rb_tab_home);
		rb[1] = (RadioButton) findViewById(R.id.rb_tab_consult);
		rb[2] = (RadioButton) findViewById(R.id.rb_tab_mine);
		rb[3] = (RadioButton) findViewById(R.id.rb_tab_find);
	}

	/**
	 * 初始化Fragment,设置主Fragment
	 * 
	 * @author yangchj
	 * @date 2014-10-26 上午12:52:41
	 */
	private void initFragment() {
		fMgr = getSupportFragmentManager();
		FragmentTransaction ft = fMgr.beginTransaction();
		if (mainFragment == null) {
			mainFragment = new MainFragment();
			ft.add(R.id.tabcontent, mainFragment, "mainFragment");
			ft.addToBackStack("mainFragment");
			ft.commit();
		}
	}

	/**
	 * 设置当前选中的Fragment
	 * 
	 * @param cur
	 */
	private void setCurrFragment(int cur) {
		FragmentTransaction ft = fMgr.beginTransaction();
		switch (cur) {
		case 0:
			if (fMgr.findFragmentByTag("mainFragment") != null
					&& fMgr.findFragmentByTag("mainFragment").isVisible()) {
				return;
			}
			ft.replace(R.id.tabcontent, mainFragment, "mainFragment");
			ft.commitAllowingStateLoss();
			break;

		case 1:
			if (consultFragment== null) {
				consultFragment=new ConsultFragment();
			}
			ft.replace(R.id.tabcontent, consultFragment, "consultFragment");
			ft.commitAllowingStateLoss();
			break;
		case 2:
			if (mineFragment== null) {
				mineFragment=new MineFragment();
			}
			ft.replace(R.id.tabcontent, mineFragment, "mineFragment");
			ft.commitAllowingStateLoss();
			break;
		case 3:
			if (findFragment== null) {
				findFragment=new FindFragment();
			}
			ft.replace(R.id.tabcontent, findFragment, "findFragment");
			ft.commitAllowingStateLoss();
			break;
		}
	}
	/**
	 * 设置选中tab项
	 * @author yangchj
	 * @date 2014-10-26 上午2:15:03
	 * @param position
	 */
	private void setCheckedItem(int position){
		switch (position) {
		case 0:
			rg.check(R.id.rb_tab_home);
			break;
		case 1:
			rg.check(R.id.rb_tab_consult);
			break;
		case 2:
			rg.check(R.id.rb_tab_mine);
			break;
		case 3:
			rg.check(R.id.rb_tab_find);
			break;			
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setCheckedItem(curr_item);
	}

	/**
	 * 设置监听事件
	 * 
	 * @author yangchj
	 * @date 2014-10-26 上午12:47:50
	 */
	private void setListener() {
		//监听tab切换
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_tab_home:
					curr_item=0;
					break;
				case R.id.rb_tab_consult:
					curr_item=1;
					break;
				case R.id.rb_tab_mine:
					curr_item=2;
					break;
				case R.id.rb_tab_find:
					curr_item=3;
					break;					
				}
				setCurrFragment(curr_item);
			}
		});
	}

}
