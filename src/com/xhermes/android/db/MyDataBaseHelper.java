package com.xhermes.android.db;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDataBaseHelper extends SQLiteOpenHelper{
	public static final int VERSION = 2;
	public static final String TABLE_NAME = "TravelInfo";  //表名
	public static final String TABLE_NAME2 = "PositionData";
	private static final String TABLE_NAME3 = "OBDData";
	private static final String TABLE_NAME4 = "Notice";
	public static final String dbPath = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath()+"/XHermes";
	public static final String DB_NAME = dbPath + "/" + "XHermes.db";
	


	String SQL1 = "CREATE TABLE " + TABLE_NAME + 
			"([id] INTEGER PRIMARY KEY AUTOINCREMENT," +
			"[eqid] VARCHAR(50)," +
			"[starttime] VARCHAR(50),"+
			"[endtime] VARCHAR(50),"+
			"[distance] VARCHAR(50),"+
			"[maxSpeed] VARCHAR(50)," +
			"[timeOut] VARCHAR(50)," +
			"[brakes] VARCHAR(50)," +
			"[emBrakes] VARCHAR(50)," +
			"[speedUp] VARCHAR(50)," +
			"[emSpeedUp] VARCHAR(50)," +
			"[averSpeed] VARCHAR(50)," +
			"[waterTemp] VARCHAR(50)," +
			"[rpm] VARCHAR(50)," +
			"[voltage] VARCHAR(50)," +
			"[totalFuelC] VARCHAR(50)," +
			"[averFuelC] VARCHAR(50)," +
			"[tiredTime] VARCHAR(50),"+
			"[travelID] VARCHAR(50),"+
			"[sposition] VARCHAR(200),"+
			"[eposition] VARCHAR(200)"+
			" );";

	String SQL2="CREATE TABLE " + TABLE_NAME2 + 
			"([id] INTEGER PRIMARY KEY AUTOINCREMENT," +
			"[eqid] VARCHAR(50)," +
			"[lat] VARCHAR(50),"+
			"[lon] VARCHAR(50),"+
			"[angle] VARCHAR(50),"+
			"[time] VARCHAR(50) UNIQUE"+
			" );";
	
	String SQL3="CREATE TABLE " + TABLE_NAME3 + 
			"([id] INTEGER PRIMARY KEY AUTOINCREMENT," +
			"[eqid] VARCHAR(50)," +
			"[Ospeed] VARCHAR(50),"+
			"[Ovoltage] VARCHAR(50),"+
			"[OwaterTemp] VARCHAR(50),"+
			"[Orpm] VARCHAR(50)"+
			" );";

	String SQL4="CREATE TABLE " + TABLE_NAME4 + 
			"([id] INTEGER PRIMARY KEY AUTOINCREMENT," +
			"[nid] VARCHAR(50)," +
			"[ntype] VARCHAR(50),"+
			"[ncontent] VARCHAR(50),"+
			"[nlevel] VARCHAR(2)"+
			" );";
	
	private File dbf;
	
	/**  
	 * (Context context, String name, CursorFactory factory,int version)  
	 * @param context 上下文对象  
	 * @param name 数据库名称  
	 * @param factory  游标工厂  
	 * @param version 数据库版本  
	 */  
	public MyDataBaseHelper(Context context, String name,CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public MyDataBaseHelper(Context context){
		this(context,DB_NAME,null, VERSION);
	}
	
	/**数据库第一次被使用时创建数据库  
	 * @param db 操作数据库的  
	 */ 
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL1);
		db.execSQL(SQL2);
		db.execSQL(SQL3);
		Log.d("db","create table success");
	}

	/**数据库版本发生改变时才会被调用,数据库在升级时才会被调用;  
	 * @param db 操作数据库  
	 * @param oldVersion 旧版本  
	 * @param newVersion 新版本  
	 */  
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if(newVersion!=oldVersion){
			db.execSQL("drop table if exists "+TABLE_NAME);  
			db.execSQL("drop table if exists "+TABLE_NAME2);
			db.execSQL("drop table if exists "+TABLE_NAME3);
			onCreate(db);
		}
	}

	public void close(){
		if(dbf.exists())
			SQLiteDatabase.openOrCreateDatabase(dbf, null).close();
	}



	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		File dbp = new File(dbPath);
		Log.d("MyDataBaseHelper",DB_NAME);
		dbf = new File(DB_NAME);
		if (!dbp.exists()) {
			dbp.mkdir();
		}
		// 数据库文件是否创建成功
		boolean isFileCreateSuccess = false;
		if (!dbf.exists()) {
			try {
				isFileCreateSuccess = dbf.createNewFile();
				if(isFileCreateSuccess){
					onCreate(SQLiteDatabase.openOrCreateDatabase(dbf, null));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			isFileCreateSuccess = true;
		}
		if (isFileCreateSuccess) {
			return SQLiteDatabase.openOrCreateDatabase(dbf, null);
		}
		else {
			return null;
		}
	}

}
