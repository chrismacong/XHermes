package com.xhermes.android.model;

public class OBDData {
	private int id;
	private String eqid;
	//速度
	private String Ospeed;
	//电压
	private String Ovoltage;
	//发动机水温
	private String OwaterTemp;
	//发动机转速
	private String Orpm;

	public OBDData(){

	}

	public OBDData(String str){
		String[] data=str.split(";");
		Ospeed=data[0];
		Ovoltage=data[1];
		OwaterTemp=data[2];
		Orpm=data[3];
	}

	public String getOspeed() {
		return Ospeed;
	}
	public void setOspeed(String ospeed) {
		Ospeed = ospeed;
	}
	public String getOvoltage() {
		return Ovoltage;
	}
	public void setOvoltage(String ovoltage) {
		Ovoltage = ovoltage;
	}
	public String getOwaterTemp() {
		return OwaterTemp;
	}
	public void setOwaterTemp(String waterTemp) {
		this.OwaterTemp = waterTemp;
	}
	public String getOrpm() {
		return Orpm;
	}
	public void setOrpm(String rpm) {
		this.Orpm = rpm;
	}
	public int getid() {
		return id;
	}
	public void setid(int id) {
		this.id = id;
	}

	public String getEqid() {
		return eqid;
	}

	public void setEqid(String eqid) {
		this.eqid = eqid;
	}

}
