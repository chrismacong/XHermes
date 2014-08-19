package com.xhermes.android.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.xhermes.android.db.MyDataBaseHelper;
import com.xhermes.android.model.TravelInfo;

public class TravelInfoDao  extends Dao{
	String TBL_NAME="TravelInfo";
	
	public TravelInfoDao(Context ctx){
		helper=new MyDataBaseHelper(ctx);
	}
	
//	"([id] INTEGER PRIMARY KEY AUTOINCREMENT," +
//	"[starttime] VARCHAR(50),"+
//	"[endtime] VARCHAR(50),"+
//	"[distance] VARCHAR(50),"+
//	"[maxSpeed] VARCHAR(50)," +
//	"[timeOut] VARCHAR(50)," +
//	"[brakes] VARCHAR(50)," +
//	"[emBrakes] VARCHAR(50)," +
//	"[speedUp] VARCHAR(50)," +
//	"[emSpeedUp] VARCHAR(50)," +
//	"[averSpeed] VARCHAR(50)," +
//	"[waterTemp] VARCHAR(50)," +
//	"[rpm] VARCHAR(50)," +
//	"[voltage] VARCHAR(50)," +
//	"[totalFuelC] VARCHAR(50)," +
//	"[averFuelC] VARCHAR(50)," +
//	"[tiredTime] VARCHAR(50)"+
//	"[travelID] VARCHAR(50)"+
//	"[sposition] VARCHAR(200)"+
//	"[eposition] VARCHAR(200)"+
	
	public ArrayList<TravelInfo> query(String eqid,String orderBy,String limit){
		db=helper.getReadableDatabase();
		ArrayList<TravelInfo> list=new ArrayList<TravelInfo>();
		Cursor cursor=db.query(TBL_NAME, null, null, null, null, null, orderBy,limit);
		while(cursor.moveToNext()){
			TravelInfo tempData =new TravelInfo();
			tempData.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
			tempData.setEqid(cursor.getString(cursor.getColumnIndex("eqid")));
			tempData.setStarttime(cursor.getString(cursor.getColumnIndex("starttime")));
			tempData.setEndtime(cursor.getString(cursor.getColumnIndex("endtime")));
			tempData.setDistance(cursor.getString(cursor.getColumnIndex("distance")));
			tempData.setMaxSpeed(cursor.getString(cursor.getColumnIndex("maxSpeed")));
			tempData.setTimeOut(cursor.getString(cursor.getColumnIndex("timeOut")));
			tempData.setBrakes(cursor.getString(cursor.getColumnIndex("brakes")));
			tempData.setEmBrakes(cursor.getString(cursor.getColumnIndex("emBrakes")));
			tempData.setSpeedUp(cursor.getString(cursor.getColumnIndex("speedUp")));
			tempData.setEmSpeedUp(cursor.getString(cursor.getColumnIndex("emSpeedUp")));
			tempData.setAverSpeed(cursor.getString(cursor.getColumnIndex("averSpeed")));
			tempData.setWaterTemp(cursor.getString(cursor.getColumnIndex("waterTemp")));
			tempData.setRpm(cursor.getString(cursor.getColumnIndex("rpm")));
			tempData.setVoltage(cursor.getString(cursor.getColumnIndex("voltage")));
			tempData.setTotalFuelC(cursor.getString(cursor.getColumnIndex("totalFuelC")));
			tempData.setAverFuelC(cursor.getString(cursor.getColumnIndex("averFuelC")));
			tempData.setTiredTime(cursor.getString(cursor.getColumnIndex("tiredTime")));
			tempData.setTravelID(cursor.getString(cursor.getColumnIndex("travelID")));
			tempData.setSposition(cursor.getString(cursor.getColumnIndex("sposition")));
			tempData.setEposition(cursor.getString(cursor.getColumnIndex("eposition")));
			list.add(tempData);
		}
		if(cursor!=null)
			cursor.close();
		db.close();
		return list;
	}
	
	public ArrayList<TravelInfo> queryByDate(String eqid,String orderBy,String limit,String start,String end){
		db=helper.getReadableDatabase();
		ArrayList<TravelInfo> list=new ArrayList<TravelInfo>();
		Cursor cursor=db.query(TBL_NAME, null, "starttime >=? and endtime<=?", new String[]{start,end}, null, null, orderBy,limit);
		while(cursor.moveToNext()){
			TravelInfo tempData =new TravelInfo();
			tempData.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
			tempData.setEqid(cursor.getString(cursor.getColumnIndex("eqid")));
			tempData.setStarttime(cursor.getString(cursor.getColumnIndex("starttime")));
			tempData.setEndtime(cursor.getString(cursor.getColumnIndex("endtime")));
			tempData.setDistance(cursor.getString(cursor.getColumnIndex("distance")));
			tempData.setMaxSpeed(cursor.getString(cursor.getColumnIndex("maxSpeed")));
			tempData.setTimeOut(cursor.getString(cursor.getColumnIndex("timeOut")));
			tempData.setBrakes(cursor.getString(cursor.getColumnIndex("brakes")));
			tempData.setEmBrakes(cursor.getString(cursor.getColumnIndex("emBrakes")));
			tempData.setSpeedUp(cursor.getString(cursor.getColumnIndex("speedUp")));
			tempData.setEmSpeedUp(cursor.getString(cursor.getColumnIndex("emSpeedUp")));
			tempData.setAverSpeed(cursor.getString(cursor.getColumnIndex("averSpeed")));
			tempData.setWaterTemp(cursor.getString(cursor.getColumnIndex("waterTemp")));
			tempData.setRpm(cursor.getString(cursor.getColumnIndex("rpm")));
			tempData.setVoltage(cursor.getString(cursor.getColumnIndex("voltage")));
			tempData.setTotalFuelC(cursor.getString(cursor.getColumnIndex("totalFuelC")));
			tempData.setAverFuelC(cursor.getString(cursor.getColumnIndex("averFuelC")));
			tempData.setTiredTime(cursor.getString(cursor.getColumnIndex("tiredTime")));
			tempData.setTravelID(cursor.getString(cursor.getColumnIndex("travelID")));
			tempData.setSposition(cursor.getString(cursor.getColumnIndex("sposition")));
			tempData.setEposition(cursor.getString(cursor.getColumnIndex("eposition")));
			list.add(tempData);
		}
		if(cursor!=null)
			cursor.close();
		db.close();
		return list;
	}
	
	public boolean insert(TravelInfo info){
		db=helper.getReadableDatabase();
		ContentValues value=new ContentValues();
		value.put("eqid", info.getEqid());
		value.put("starttime", info.getStarttime());
		value.put("endtime",info.getEndtime());
		value.put("distance", info.getDistance());
		value.put("maxSpeed",info.getMaxSpeed());
		value.put("timeOut",info.getTimeOut());
		value.put("brakes",info.getBrakes());
		value.put("emBrakes",info.getEmBrakes());
		value.put("speedUp", info.getSpeedUp());
		value.put("emSpeedUp", info.getEmSpeedUp());
		value.put("averSpeed",info.getAverSpeed());
		value.put("waterTemp",info.getWaterTemp());
		value.put("rpm",info.getRpm());
		value.put("voltage",info.getVoltage());
		value.put("totalFuelC",info.getTotalFuelC());
		value.put("averFuelC",info.getAverFuelC());
		value.put("tiredTime",info.getTiredTime());
		value.put("travelID",info.getTravelID());
		value.put("sposition", info.getSposition());
		value.put("eposition", info.getEposition());
		long rowid = -1;
		try{
			rowid=db.insertOrThrow(TBL_NAME, null, value);
			//isOutOfRange(TBL_NAME,info.getEqid());
		}catch(Exception e){
			if(e.getClass().equals(SQLiteConstraintException.class))
				Log.d("PositionDataDao","insert same data");
		}
		db.close();
		
		if(rowid!=-1)
			return true;
		else
			return false;
	}
	
	public boolean delete(){
		db=helper.getReadableDatabase();
		db.close();
		return false;
	}
}
