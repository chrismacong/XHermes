package com.xhermes.android.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.xhermes.android.db.MyDataBaseHelper;
import com.xhermes.android.model.OBDData;
import com.xhermes.android.model.OBDParameters;

public class OBDParametersDao extends Dao{
	String TBL_NAME="OBDParameters";
	public OBDParametersDao(Context ctx){
		helper=new MyDataBaseHelper(ctx);
	}
	
	public ArrayList<OBDParameters> query(String eqid,String orderBy,String limit){
		db=helper.getReadableDatabase();
		ArrayList<OBDParameters> list=new ArrayList<OBDParameters>();
		Cursor cursor=db.query(TBL_NAME, null,  "eqid=?", new String[]{eqid}, null, null, orderBy,limit);
		while(cursor.moveToNext()){
			OBDParameters tempData =new OBDParameters();
			tempData.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
			tempData.setEqid(cursor.getString(cursor.getColumnIndex("eqid")));
			tempData.setCurrent_miles(cursor.getString(cursor.getColumnIndex("current_miles")));
			tempData.setMaintenance_gap(cursor.getString(cursor.getColumnIndex("maintenance_gap")));
			tempData.setMaintenance_next(cursor.getString(cursor.getColumnIndex("maintenance_next")));
			tempData.setTime(cursor.getString(cursor.getColumnIndex("time")));
			list.add(tempData);
		}
		if(cursor!=null)
			cursor.close();
		db.close();
		return list;
	}
	
//	"([oid] INTEGER PRIMARY KEY AUTOINCREMENT," +
//	"[Ospeed] VARCHAR(50)"+
//	"[Ovoltage] VARCHAR(50)"+
//	"[OwaterTemp] VARCHAR(50)"+
//	"[Orpm] VARCHAR(50)"+
	public boolean insert(OBDParameters data){
		db=helper.getReadableDatabase();
		ContentValues value=new ContentValues();
		value.put("Current_miles", data.getCurrent_miles());
		value.put("maintenance_gap",data.getMaintenance_gap());
		value.put("maintenance_next", data.getMaintenance_next());
		value.put("time",data.getTime());
		value.put("eqid",data.getEqid());
		
		long rowid = -1;
		try{
			rowid=db.insertOrThrow(TBL_NAME, null, value);
			//isOutOfRange(TBL_NAME,data.getEqid());
		}catch(Exception e){
			if(e.getClass().equals(SQLiteConstraintException.class))
				Log.d("OBDParameters","insert same data");
		}
		
		db.close();
		if(rowid!=-1)
			return true;
		else
			return false;
	}
	
	public boolean delete(){
		db=helper.getReadableDatabase();
		return false;
	}
}
