package com.android.travel.db;

import com.android.travel.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/***
 * 数据库操作类
 * @author yangchj
 * @date 2014年11月3日 下午9:16:12
 */
public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME ="travel.db";
	private static final String TB_USER="tb_user";//管理员表
	private static final String TB_CUSTOM="tb_custom";//顾客表
	private static final String TB_MENU="tb_menu";//菜单表（项目）
	private static final String TB_REGISTER="tb_register";//顾客登记表	
	private static final String TB_REGISTER_DETAIL="tb_register_detail";
	
	private static final String CREATE_TB_USER="create table tb_user(_id integer primary key autoincrement,uname varchar(20),pwd varchar(20))";
	
	private static final String CREATE_TB_CUSTOM="create table tb_custom(_id integer primary key autoincrement,cname varchar(20),cphone varchar(20))";
	
	private static final String CREATE_TB_MENU="create table tb_menu(_id integer primary key autoincrement,mname varchar(50),mtype varchar(32),mprice double,mremark text)";
	
	private static final String CREATE_TB_REGISTER="create table tb_register(_id integer primary key autoincrement,cid integer,register_time timestamp)";
	
	private static final String CREATE_TB_REGISTER_DETAIL="create table tb_register_detail(_id integer primary key autoincrement,rid integer,mid integer,person_num integer)";
	private SQLiteDatabase db;
	public DBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db=db;
		db.execSQL(CREATE_TB_USER);  
		db.execSQL(CREATE_TB_CUSTOM);
		db.execSQL(CREATE_TB_MENU);
		db.execSQL(CREATE_TB_REGISTER);
		db.execSQL(CREATE_TB_REGISTER_DETAIL);
		LogUtil.i("创建travel.db数据库,建立用户表,顾客表,菜单表,登记表,登记详情表");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	/**
	 * 用户表
	 * @author yangchj
	 * @date 2014年11月3日 下午10:02:21
	 * @param values
	 */
	public long insertUser(ContentValues values) {  
		 long res=-1;
	     SQLiteDatabase db = getWritableDatabase();  
	     res=db.insert(TB_USER, null, values);  
	     db.close(); 
	     return res;
	}  
	/**
	 * 修改用户密码
	 * @author yangchj
	 * @date 2014年11月3日 下午10:10:50
	 * @param values
	 */
	public void updateUser(ContentValues values){
		 SQLiteDatabase db = getWritableDatabase();
		 db.update(TB_USER, values, " _id=? ", new String[]{"1"});
	}
	
	public Cursor queryUser() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(TB_USER, null, " _id=? ", new String[]{"1"}, null, null, null);
		return cursor;
	}
	
	/**
	 * 新增顾客
	 * @author yangchj
	 * @date 2014年11月3日 下午10:13:54
	 * @param values
	 */
	public long insertCustomer(ContentValues values){
		 long res=-1;
	     SQLiteDatabase db = getWritableDatabase();  
	     res=db.insert(TB_CUSTOM, null, values);  
	     db.close(); 
	     return res;
	}
	/**
	 * 修改顾客表
	 * @author yangchj
	 * @date 2014年11月3日 下午10:18:02
	 */
	public void updateCustomer(ContentValues values,String cid){
		SQLiteDatabase db = getWritableDatabase();
		db.update(TB_CUSTOM, values, "_id=? ", new String[]{cid});
	}
	/**
	 * 查询顾客信息
	 * @author yangchj
	 * @date 2014年11月3日 下午10:34:23
	 * @param selection
	 * @param selectionArgs
	 * @param orderBy
	 * @return
	 */
	public Cursor queryCustomers(String selection,String[] selectionArgs,String orderBy){
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(TB_CUSTOM, null, selection, selectionArgs, null, null, orderBy);
		return cursor;
	}
	/**
	 * 删除顾客信息
	 * @author yangchj
	 * @date 2014年11月3日 下午10:39:58
	 * @param cid
	 */
	public void delCustomer(String cid){
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TB_CUSTOM, "_id=?", new String[] {cid});
	}
	/**
	 * 新增菜单项
	 * @author yangchj
	 * @date 2014年11月3日 下午10:41:33
	 * @param values
	 * @return
	 */
	public long insertMenu(ContentValues values){
		 long res=-1;
	     SQLiteDatabase db = getWritableDatabase();  
	     res=db.insert(TB_MENU, null, values);  
	     db.close(); 
	     return res;
	}
	/**
	 * 更新菜单
	 * @author yangchj
	 * @date 2014年11月3日 下午10:56:12
	 * @param values
	 * @param mid
	 */
	public void updateMenu(ContentValues values,String mid){
		SQLiteDatabase db = getWritableDatabase();
		db.update(TB_MENU, values, "_id=?", new String[]{mid});
	}
	/**
	 * 删除菜单项
	 * @author yangchj
	 * @date 2014年11月3日 下午10:55:51
	 * @param mid
	 */
	public void delMenu(String mid){
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TB_MENU, "_id=?", new String[]{mid});
	}
	/**
	 * 查询菜单项
	 * @author yangchj
	 * @date 2014年11月3日 下午10:59:00
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public Cursor queryMenus(String selection,String[]selectionArgs){
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(TB_CUSTOM, null, selection, selectionArgs, null, null, null);
		return cursor;
	}
	/**
	 * 关闭数据库
	 */
    public void close() {  
        if (db != null)  
            db.close();  
    } 
}
