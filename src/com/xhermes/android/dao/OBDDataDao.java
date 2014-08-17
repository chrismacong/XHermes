package com.xhermes.android.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xhermes.android.db.MyDataBaseHelper;
import com.xhermes.android.model.OBDData;

public class OBDDataDao extends Dao{
	String TBL_NAME="OBDData";
	public OBDDataDao(Context ctx){
		helper=new MyDataBaseHelper(ctx);
	}
	
	public ArrayList<OBDData> query(String eqid,String orderBy,String limit){
		db=helper.getReadableDatabase();
		ArrayList<OBDData> list=new ArrayList<OBDData>();
		Cursor cursor=db.query(TBL_NAME, null,  "eqid=?", new String[]{eqid}, null, null, orderBy,limit);
		while(cursor.moveToNext()){
			OBDData tempData =new OBDData();
			tempData.setid(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
			tempData.setEqid(cursor.getString(cursor.getColumnIndex("eqid")));
			tempData.setOrpm(cursor.getString(cursor.getColumnIndex("Orpm")));
			tempData.setOspeed(cursor.getString(cursor.getColumnIndex("Ospeed")));
			tempData.setOvoltage(cursor.getString(cursor.getColumnIndex("Ovoltage")));
			tempData.setOwaterTemp(cursor.getString(cursor.getColumnIndex("OwaterTemp")));
			tempData.setOpressure(cursor.getString(cursor.getColumnIndex("Opressure")));
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
	public boolean insert(OBDData data){
		db=helper.getReadableDatabase();
		ContentValues value=new ContentValues();
		value.put("Ospeed", data.getOspeed());
		value.put("Ovoltage",data.getOvoltage());
		value.put("OwaterTemp", data.getOwaterTemp());
		value.put("Orpm",data.getOrpm());
		value.put("Opressure", data.getOpressure());
		value.put("eqid", data.getEqid());
		value.put("time", data.getTime());
		
		long rowid=db.insert(TBL_NAME, null, value);
		isOutOfRange(TBL_NAME,data.getEqid());
		
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
