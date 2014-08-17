package com.xhermes.android.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xhermes.android.db.MyDataBaseHelper;
import com.xhermes.android.model.PositionData;

public class PositionDataDao extends Dao{
	String TBL_NAME="PositionData";
	
	public PositionDataDao(Context ctx){
		helper=new MyDataBaseHelper(ctx);
	}

	public ArrayList<PositionData> query(String eqid,String orderBy,String limit){
		db=helper.getReadableDatabase();
		ArrayList<PositionData> list=new ArrayList<PositionData>();
		Cursor cursor=db.query(TBL_NAME, null, "eqid=?", new String[]{eqid}, null, null, orderBy,limit);
		while(cursor.moveToNext()){
			PositionData tempData =new PositionData();
			tempData.setid(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
			tempData.setEqid(cursor.getString(cursor.getColumnIndex("eqid")));
			tempData.setLat(cursor.getString(cursor.getColumnIndex("lat")));
			tempData.setLon(cursor.getString(cursor.getColumnIndex("lon")));
			tempData.setAngle(cursor.getString(cursor.getColumnIndex("angle")));
			tempData.setTime(cursor.getString(cursor.getColumnIndex("time")));
			list.add(tempData);
		}
		if(cursor!=null)
			cursor.close();
		db.close();
		return list;
	}

	public boolean insert(PositionData p){
		db=helper.getReadableDatabase();
		ContentValues value=new ContentValues();
		value.put("time", p.getTime());
		value.put("eqid", p.getEqid());
		value.put("lon",p.getLon());
		value.put("lat", p.getLat());
		value.put("angle", p.getAngle());
		
		long rowid=db.insert(TBL_NAME, null, value);
		isOutOfRange(TBL_NAME,p.getEqid());
		
		db.close();
		if(rowid!=-1)
			return true;
		else
			return false;
	}
	
	public boolean insert(String[] str){
		db=helper.getReadableDatabase();
		ContentValues value=new ContentValues();
		value.put("lat", str[0]);
		value.put("lon", str[1]);
		value.put("angle", str[2]);
		value.put("eqid", str[3]);
		value.put("time", str[4]);
		long rowid=db.insert(TBL_NAME, null, value);
		//查看是否超出最大记录数
		isOutOfRange(TBL_NAME,str[3]);
		
		db.close();
		if(rowid!=-1)
			return true;
		else
			return false;
	}
	
	public boolean deleteById(int id){
		db=helper.getReadableDatabase();
		int result=db.delete(TBL_NAME, "id=?", new String[]{id+""});
		
		db.close();
		if(result==-1)
			return false;
		else
			return true;
	}
}
