package com.xhermes.android.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xhermes.android.db.MyDataBaseHelper;
import com.xhermes.android.model.Notice;
import com.xhermes.android.model.OBDData;
import com.xhermes.android.model.OBDParameters;

public class NoticeDao extends Dao{
	String TBL_NAME="Notice";
	public NoticeDao(Context ctx){
		helper=new MyDataBaseHelper(ctx);
	}

	public ArrayList<Notice> query(String eqid,String orderBy,String limit){
		db=helper.getReadableDatabase();
		ArrayList<Notice> list=new ArrayList<Notice>();
		Cursor cursor=db.query(TBL_NAME, null,  "eqid=?", new String[]{eqid}, null, null, orderBy,limit);
		while(cursor.moveToNext()){
			Notice tempData =new Notice();
			tempData.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
			tempData.setEqid(cursor.getString(cursor.getColumnIndex("eqid")));
			tempData.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			tempData.setContent(cursor.getString(cursor.getColumnIndex("content")));
			tempData.setRead(cursor.getInt(cursor.getColumnIndex("isRead")));
			tempData.setTime(cursor.getString(cursor.getColumnIndex("time")));
			list.add(tempData);
		}
		if(cursor!=null)
			cursor.close();
		db.close();
		return list;
	}

	public int queryReadOrNot(String eqid,String arg){
		db=helper.getReadableDatabase();
		Cursor cursor=db.query(TBL_NAME, null, "isRead=?",new String[]{arg}, null, null, null);
		return cursor.getCount();
	}
	
	public boolean insert(Notice data){
		db=helper.getReadableDatabase();
		ContentValues value=new ContentValues();
		value.put("title", data.getTitle());
		value.put("content",data.getContent());
		value.put("sender", data.getSender());
		value.put("time",data.getTime());
		value.put("eqid",data.getEqid());
		value.put("isRead",data.isRead());

		long rowid = -1;
		try{
			rowid=db.insertOrThrow(TBL_NAME, null, value);
			//isOutOfRange(TBL_NAME);
		}catch(Exception e){
			
		}
		db.close();
		if(rowid!=-1)
			return true;
		else
			return false;
	}

	public boolean updateToRead(int id,int isRead){
		db=helper.getReadableDatabase();
		ContentValues value=new ContentValues();
		value.put("isRead", isRead);
		int num=db.update(TBL_NAME, value, "id= ?", new String[]{id+""});
		if(num>0)
			return true;
		else
			return false;
	}

	public boolean delete(){
		db=helper.getReadableDatabase();
		return false;
	}
}
