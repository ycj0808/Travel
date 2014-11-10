package com.android.travel.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.travel.util.LogUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 登记相关
 * @author yangchj
 *create table tb_register(_id integer primary key autoincrement,cid integer,register_time timestamp)
 */
public class RegisterService {

	private DBHelper helper;
	public RegisterService(Context context){
		helper=new DBHelper(context);
	}
	/**
	 * 添加用户登记
	 * @param cid
	 */
	public void addRegister(int cid){//tb_register
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="insert into tb_register(cid) values(?)";
		db.execSQL(sql, new Object[]{cid});
		LogUtil.i("用户登记...");
	}
	/**
	 * 查询
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public List<Map<String,Object>> getRegisters(int firstResult,int maxResult){
		SQLiteDatabase db=helper.getWritableDatabase();
		int count=getRegistersCounts();
		List<Map<String,Object>> listRegisters=new ArrayList<Map<String,Object>>();
		String sql="select r._id,r.cid,c.cname,c.cphone,datetime(r.register_time,'localtime') register_time from tb_custom c inner join tb_register r on r.cid=c._id limit ? offset ?";
		Cursor cursor=db.rawQuery(sql, new String[]{String.valueOf(maxResult),String.valueOf(firstResult)});
		while(cursor.moveToNext()){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("rid", cursor.getInt(0));
			map.put("cid", cursor.getInt(1));
			map.put("cname", cursor.getString(2));
			map.put("cphone", cursor.getString(3));
			map.put("register_time", cursor.getString(4));
			map.put("count", count);
			listRegisters.add(map);
		}
		cursor.close();
		LogUtil.i("查询用户登记纪录:"+firstResult+listRegisters.toString());
		return listRegisters;
	}
	/**
	 * 获取登记数量
	 * @return
	 */
	public int getRegistersCounts(){
		int count=0;
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="select count(r._id) from tb_custom c inner join tb_register r on r.cid=c._id";
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.moveToNext()){
			count=cursor.getInt(0);
		}
		cursor.close();
		LogUtil.i("获取登记数量:"+count);
		return count;
	}
	/**
	 * 通过用户姓名查询
	 * @param name
	 * @return
	 */
	public List<Map<String,Object>> getRegistersByName(String name){
		SQLiteDatabase db=helper.getWritableDatabase();
		List<Map<String,Object>> listRegisters=new ArrayList<Map<String,Object>>();
		Cursor cursor=db.rawQuery("select r._id,cid,cname,cphone,datetime(r.register_time,'localtime') register_time from tb_custom c inner join tb_register r on r.cid=c._id and c.cname like ?", new String[]{"%"+name+"%"});
		while(cursor.moveToNext()){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("rid", cursor.getInt(0));
			map.put("cid", cursor.getInt(1));
			map.put("cname", cursor.getString(2));
			map.put("cphone", cursor.getString(3));
			map.put("register_time", cursor.getString(4));
			listRegisters.add(map);
		}
		cursor.close();
		LogUtil.i("查询用户登记纪录:"+name);
		return listRegisters;	
	}
	/**
	 * 按手机号查询
	 * @param phone
	 * @return
	 */
	public List<Map<String,Object>> getRegistersByPhone(String phone){
		SQLiteDatabase db=helper.getWritableDatabase();
		List<Map<String,Object>> listRegisters=new ArrayList<Map<String,Object>>();
		Cursor cursor=db.rawQuery("select r._id,cid,cname,cphone,datetime(r.register_time,'localtime') register_time from tb_custom c inner join tb_register r on r.cid=c._id and c.cphone like ? order by register_time desc", new String[]{"%"+phone+"%"});
		while(cursor.moveToNext()){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("rid", cursor.getInt(0));
			map.put("cid", cursor.getInt(1));
			map.put("cname", cursor.getString(2));
			map.put("cphone", cursor.getString(3));
			map.put("register_time", cursor.getString(4));
			listRegisters.add(map);
		}
		cursor.close();
		LogUtil.i("查询用户登记纪录:"+phone);
		return listRegisters;			
	}
	
	/****************************************
				注册详情相关操作
	tb_register_detail(_id integer primary key autoincrement,rid integer,mid integer,person_num integer)			
	****************************************/
	public void addRegisterDetail(int rid,int mid,int num){
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="insert into tb_register_detail(rid,mid,person_num) values(?,?,?)";
		db.execSQL(sql, new Object[]{rid,mid,num});
		LogUtil.i("添加用户登记详情纪录...");
	}
	/**
	 * 查询用户登记项纪录
	 * @param rid
	 * @return
	 */
	public List<Map<String,Object>> getRegisterDetails(int rid){
		SQLiteDatabase db=helper.getWritableDatabase();
		List<Map<String,Object>> listData=new ArrayList<Map<String,Object>>();
		Cursor cursor=db.rawQuery("select rd._id,rd.rid,rd.mid,m.mname,m.mtype,m.mprice,m.mremark,rd.person_num from tb_register_detail rd inner join tb_register r join tb_menu m on rd.rid=r._id and rd.mid=m._id and rd.rid=?", new String[]{String.valueOf(rid)});
		while(cursor.moveToNext()){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("rdid", cursor.getInt(0));
			map.put("rid", cursor.getInt(1));
			map.put("mid", cursor.getInt(2));
			map.put("mname", cursor.getString(3));
			map.put("mtype", cursor.getString(4));
			map.put("mprice", cursor.getDouble(5));
			map.put("mremark", cursor.getString(6));
			map.put("person_num", cursor.getInt(7));
			listData.add(map);
		}
		cursor.close();
		LogUtil.i("查询用户登记详情纪录"+listData.toString());
		return listData;
	}
}
