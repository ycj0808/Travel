package com.android.travel.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.travel.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 *  菜单操作
 * @author yangchj
 *tb_menu(_id integer primary key autoincrement,mname varchar(50),mtype varchar(32),mprice double,mremark text)
 */
public class MenuService {

	private DBHelper helper;
	public MenuService(Context context){
		helper=new DBHelper(context);
	}
	/**
	 * 添加菜单
	 */
	public long addMenu(String mname,String mtype,double mprice,String mremark){
		long res=-1;
		SQLiteDatabase db=helper.getWritableDatabase();
//		String sql="insert into tb_menu(mname,mtype,mprice,mremark) values(?,?,?,?)";
//		db.execSQL(sql, new Object[]{mname,mtype,mprice,mremark});
		ContentValues values=new ContentValues();
		values.put("mname", mname);
		values.put("mtype", mtype);
		values.put("mprice", mprice);
		values.put("mremark", mremark);
		res=db.insert("tb_menu", null, values);
		LogUtil.i("添加菜单项...");
		return res;
	}
	/**
	 * 修改
	 * @param mname
	 * @param mprice
	 * @param mremark
	 */
	public void updateMenu(int mid,String mname,double mprice,String mremark){
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="update tb_menu set mname=?,mprice=?,mremark=? where _id=?";
		db.execSQL(sql, new Object[]{mname,mprice,mremark,mid});
		LogUtil.i("修改菜单项...");
	}
	/**
	 * 删除菜单
	 * @param mid
	 */
	public void delMenu(int mid){
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="delete from tb_menu where _id=?";
		db.execSQL(sql, new Object[]{mid});
		LogUtil.i("删除菜单项...");
	}
	/**
	 * 获取菜单
	 * @return
	 */
	public List<Map<String,Object>> getMenus(){
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="select _id,mname,mtype,mprice,mremark from tb_menu";
		List<Map<String,Object>> listMenus=new ArrayList<Map<String,Object>>();
		Cursor cursor=db.rawQuery(sql,null);
		while(cursor.moveToNext()){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("mid", cursor.getInt(0));
			map.put("mname", cursor.getString(1));
			map.put("mtype", cursor.getString(2));
			map.put("mprice", cursor.getDouble(3));
			map.put("mremark", cursor.getString(4));
			listMenus.add(map);
		}
		LogUtil.i("查询菜单项...");
		return listMenus;
	}
	/**
	 * 菜单名是否存在
	 * @param mname
	 * @return
	 */
	public boolean isExistsMenu(String mname){
		boolean flag=false;
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="select _id from tb_menu where mname=?";
		Cursor cursor=db.rawQuery(sql, new String[]{mname});
		flag=cursor.moveToNext();
		LogUtil.i("查询该菜单项是否已存在...");
		return flag;
	}
	
}
