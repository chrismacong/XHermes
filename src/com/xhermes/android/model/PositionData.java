package com.xhermes.android.model;


public class PositionData {
	private int id;
	private String eqid;
	private String lat;//经度
	private String lon;//纬度
	private String angle;//方向角
	private String time;//时间
	
	public PositionData(){
		
	}
	
	public PositionData(String[] str){
		lat=str[0];
		lon=str[1];
		angle=str[2];
		//SimpleDateFormat sDateFormat =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
		//time = sDateFormat.format(new java.util.Date()); 
		if(str.length>3)
			time=str[3];
	}

	public int getid() {
		return id;
	}
	public void setid(int id) {
		this.id = id;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getAngle() {
		return angle;
	}
	public void setAngle(String angle) {
		this.angle = angle;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getEqid() {
		return eqid;
	}
	public void setEqid(String eqid2) {
		this.eqid = eqid2;
	}
	
	
}
