package com.xhermes.android.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xhermes.android.db.MyDataBaseHelper;

public class Dao {
	protected MyDataBaseHelper helper;
	protected SQLiteDatabase db; 
	protected int MAXRECORDS=100;
	protected static int LIMIT =10;

	public void isOutOfRange(String table_Name){
		//Log.d("tablename",TBL_NAME+" 123123");
		Cursor cursor=	db.rawQuery(getCount(table_Name), null);
		cursor.moveToFirst();
		Long count=cursor.getLong(0);
		if(count>MAXRECORDS+LIMIT){
			db.execSQL(delMore(table_Name));
			Log.d("dao","delete more");
		}
	}
	
	public static String getCount(String table_Name){
		return "Select count(*) from "+table_Name;
	}
	
	public static String delMore(String table_Name){
		return "delete from "+table_Name+" where id in (select id from "+table_Name +" order by id asc limit "+LIMIT+")";
	}
}
