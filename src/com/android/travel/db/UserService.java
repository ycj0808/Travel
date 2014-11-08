package com.android.travel.db;

import com.android.travel.util.LogUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserService {

	private DBHelper helper;
	public UserService(Context context){
		helper=new DBHelper(context);
	}
	/**
	 * 添加用户
	 * @param uname
	 * @param pwd
	 */
	public void addUser(String uname,String pwd){
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="insert into tb_user(uname,pwd) values(?,?)";
		db.execSQL(sql, new String[]{uname,pwd});
		LogUtil.i("创建管理员用户...");
	}
	/**
	 * 修改用户密码
	 * @param pwd
	 */
	public void updateUser(String pwd,int id){
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="update tb_user set pwd=? where _id=?";
		db.execSQL(sql, new Object[]{pwd,id});
		LogUtil.i("修改管理员密码...");
	}
	/**
	 * 登陆验证
	 * @param uname
	 * @param pwd
	 * @return
	 */
	public boolean login(String uname,String pwd){
		boolean flag=false;
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="select _id,uname,pwd from tb_user where uname=? and pwd=?";
		Cursor cursor=db.rawQuery(sql, new String[]{uname,pwd});
		flag=cursor.moveToNext();
		cursor.close();
		LogUtil.i("登陆...");
		return flag;
	}
}
