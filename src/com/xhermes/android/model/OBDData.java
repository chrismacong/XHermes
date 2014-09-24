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
	//压力
	private String Opressure;

	private String time;
	public OBDData(){

	}

	public OBDData(String str){
		String t=str.replaceAll("null", "0");
		String[] data=t.split(";");
		Ospeed=data[0];
		java.text.DecimalFormat   df = new java.text.DecimalFormat("#.00");  
		Ovoltage=df.format(Double.parseDouble(data[1]) * 0.1) + "";
		Orpm=data[2];
		OwaterTemp=data[3];
		Opressure=data[4];

		time=data[5];
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

	public String getOpressure() {
		return Opressure;
	}

	public void setOpressure(String opressure) {
		Opressure = opressure;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
