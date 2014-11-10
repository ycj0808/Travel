package com.android.travel.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.travel.util.LogUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 顾客操作类
 * @author yangchj
 * tb_custom(_id integer primary key autoincrement,cname varchar(20),cphone varchar(20))";
 */
public class CustomerService {

	private DBHelper helper;
	public CustomerService(Context context){
		helper=new DBHelper(context);
	}
	/**
	 * 添加客户信息
	 */
	public int addCustomer(String cname,String cphone){
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="insert into tb_custom(cname,cphone) values(?,?)";
		db.execSQL(sql, new Object[]{cname,cphone});
		LogUtil.i("添加客户信息...");
		Map<String,Object> map=getCustomerByPhone(cphone);
		if(map==null||!map.containsKey("cid")){
			return -1;
		}else{
			return Integer.valueOf(map.get("cid").toString());
		}
	}
	/**
	 * 更新用户信息
	 * @param cid
	 * @param cname
	 * @param cphone
	 */
	public void updateCustomer(int cid,String cname,String cphone){
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="update tb_custom set cname=?,cphone=? where _id=?";
		db.execSQL(sql, new Object[]{cname,cphone,cid});
		LogUtil.i("修改客户信息...");
	}
	/**
	 * 删除客户信息
	 * @param cid
	 */
	public void delCustomer(int cid){
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="delete from tb_custom where _id=?";
		db.execSQL(sql, new Object[]{cid});
		LogUtil.i("删除客户信息...");
	}
	/**
	 * 查询客户信息
	 * @param cid
	 * @return
	 */
	public Map<String,Object> getCustomerById(String cid){
		SQLiteDatabase db=helper.getWritableDatabase();
		Map<String,Object> map=null;
		Cursor cursor=db.query("tb_custom", new String[]{"_id","cname","cphone"}, "_id=?", new String[]{cid}, null, null, null);
		if(cursor.moveToNext()){
			map=new HashMap<String,Object>();
			map.put("cid", cursor.getInt(0));
			map.put("cname", cursor.getString(1));
			map.put("cphone", cursor.getString(2));
		}
		cursor.close();
		return map;
	}
	/**
	 * 查询手机号是否已有注册纪录
	 * @return
	 */
	public Map<String,Object> getCustomerByPhone(String phone){
		SQLiteDatabase db=helper.getWritableDatabase();
		Map<String,Object> map=null;
		Cursor cursor=db.query("tb_custom", new String[]{"_id","cname","cphone"}, "cphone=?", new String[]{phone}, null, null, null);
		if(cursor.moveToNext()){
			map=new HashMap<String,Object>();
			map.put("cid", cursor.getInt(0));
			map.put("cname", cursor.getString(1));
			map.put("cphone", cursor.getString(2));
		}
		cursor.close();
		return map;
	}
}
