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
	public List<Map<String,Object>> getRegisters(int status,int firstResult,int maxResult){
		SQLiteDatabase db=helper.getWritableDatabase();
		int count=getRegistersCounts(status);
		List<Map<String,Object>> listRegisters=new ArrayList<Map<String,Object>>();
		String sql="select r._id,r.cid,c.cname,c.cphone,register_time from tb_custom c inner join tb_register r on r.cid=c._id and r.status=? order by register_time desc limit ? offset ? ";
		Cursor cursor=db.rawQuery(sql, new String[]{String.valueOf(status),String.valueOf(maxResult),String.valueOf(firstResult)});
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
	public int getRegistersCounts(int status){
		int count=0;
		SQLiteDatabase db=helper.getWritableDatabase();
		String sql="select count(r._id) from tb_custom c inner join tb_register r on r.cid=c._id and r.status=?";
		Cursor cursor=db.rawQuery(sql, new String[]{String.valueOf(status)});
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
	public List<Map<String,Object>> getRegistersByName(String name,int status){
		SQLiteDatabase db=helper.getWritableDatabase();
		List<Map<String,Object>> listRegisters=new ArrayList<Map<String,Object>>();
		Cursor cursor=db.rawQuery("select r._id,cid,cname,cphone,register_time from tb_custom c inner join tb_register r on r.cid=c._id and c.cname like ? and r.status=? order by register_time desc", new String[]{"%"+name+"%",String.valueOf(status)});
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
	public List<Map<String,Object>> getRegistersByPhone(String phone,int status){
		SQLiteDatabase db=helper.getWritableDatabase();
		List<Map<String,Object>> listRegisters=new ArrayList<Map<String,Object>>();
		Cursor cursor=db.rawQuery("select r._id,cid,cname,cphone,register_time from tb_custom c inner join tb_register r on r.cid=c._id and c.cphone like ? and r.status=? order by register_time desc", new String[]{"%"+phone+"%",String.valueOf(status)});
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
	
	/**
	 * 修改状态
	 * @param rid
	 * @return
	 */
	public int delRegister(int rid){
		int res=0;
		SQLiteDatabase db=helper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("status", 0);
		res=db.update("tb_register", values, "_id=?", new String[]{String.valueOf(rid)});
		return res;
	}
	/**
	 * 修改状态
	 * @param rid
	 * @return
	 */
	public int updateRegister(int rid){
		int res=0;
		SQLiteDatabase db=helper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("status", 2);//结账
		res=db.update("tb_register", values, "_id=?", new String[]{String.valueOf(rid)});
		return res;
	}
	
	/****************************************
				注册详情相关操作
	tb_register_detail(_id integer primary key autoincrement,rid integer,mid integer,person_num integer)			
	****************************************/
	public long addRegisterDetail(int rid,int mid,int num,int dayorhours){
		long res=-1;
		SQLiteDatabase db=helper.getWritableDatabase();
//		String sql="insert into tb_register_detail(rid,mid,person_num) values(?,?,?)";
//		db.execSQL(sql, new Object[]{rid,mid,num});
		ContentValues values=new ContentValues();
		values.put("rid", rid);
		values.put("mid", mid);
		values.put("person_num", num);
		values.put("dayorhour", dayorhours);
		res=db.insert("tb_register_detail", null, values);
		LogUtil.i("添加用户登记详情纪录...");
		return res;
	}
	/**
	 * 查询用户登记项纪录
	 * @param rid
	 * @return
	 */
	public List<Map<String,Object>> getRegisterDetails(int rid){
		SQLiteDatabase db=helper.getWritableDatabase();
		List<Map<String,Object>> listData=new ArrayList<Map<String,Object>>();
		Cursor cursor=db.rawQuery("select rd._id,rd.rid,rd.mid,m.mname,m.mtype,m.mprice,m.mremark,rd.person_num,rd.dayorhour,m.unit from tb_register_detail rd inner join tb_register r join tb_menu m on rd.rid=r._id and rd.mid=m._id and rd.rid=?", new String[]{String.valueOf(rid)});
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
			map.put("dayorhour", cursor.getInt(8));
			map.put("unit", cursor.getInt(9));
			listData.add(map);
		}
		cursor.close();
		LogUtil.i("查询用户登记详情纪录"+listData.toString());
		return listData;
	}
	/**
	 * 删除登记详情单项
	 * @param rdid
	 * @return
	 */
	public int deleteRegisterDetail(int rdid){
		int res=0;
		SQLiteDatabase db=helper.getWritableDatabase();
		res=db.delete("tb_register_detail", "_id=?", new String[]{String.valueOf(rdid)});
		return res;
	}
}
