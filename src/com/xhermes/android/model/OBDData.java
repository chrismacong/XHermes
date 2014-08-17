package com.xhermes.android.model;

public class OBDData {
	private int id;
	private String eqid;
	//�ٶ�
	private String Ospeed;
	//��ѹ
	private String Ovoltage;
	//������ˮ��
	private String OwaterTemp;
	//������ת��
	private String Orpm;
	//ѹ��
	private String Opressure;
	
	private String time;
	public OBDData(){

	}

	public OBDData(String str){
		String[] data=str.split(";");
		Ospeed=data[0];
		Ovoltage=data[1];
		OwaterTemp=data[2];
		Orpm=data[3];
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
