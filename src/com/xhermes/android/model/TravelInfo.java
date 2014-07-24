package com.xhermes.android.model;

import android.util.Log;

public class TravelInfo {
	private int id;
	private String eqid;
	//��ʼ��ֹʱ��
	private	String starttime;
	private String endtime;
	//����
	private String distance;
	//����ٶ�
	private String maxSpeed;
	//��ʱʱ��
	private String timeOut;
	//ɲ��������ɲ��
	private String brakes;
	private String emBrakes;
	//���١���������
	private String speedUp;
	private String emSpeedUp;
	//ƽ���ٶ�
	private String averSpeed;
	//������ˮ��
	private String waterTemp;
	//������ת��
	private String rpm;
	//��ѹ
	private String voltage;
	//���ͺ�
	private String totalFuelC;
	//ƽ���ͺ�
	private String averFuelC;
	//ƣ�ͼ�ʻʱ��
	private String tiredTime;
	//�г����
	private String travelID;
	//����λ�����
	private String sposition;
	//����λ���յ�
	private String eposition;
	
	public TravelInfo(){

	}

	public TravelInfo(String infoStr){
		String[] info=infoStr.split(";");
		if(info.length>0){
			endtime=info[0];
			starttime=info[1];
			distance=info[2];
			maxSpeed=info[3];
			timeOut=info[4];
			brakes=info[5];
			emBrakes=info[6];
			speedUp=info[7];
			emSpeedUp=info[8];
			averSpeed=info[9];
			waterTemp=info[10];
			rpm=info[11];
			voltage=info[12];
			totalFuelC=info[13];
			averFuelC=info[14];
			tiredTime=info[15];
			travelID=info[16];
			sposition=info[17];
			eposition=info[18];
		}
		else{
			Log.d("travelinfo","string error;"+"strlength:"+info.length);
		}
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public String getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}
	public String getBrakes() {
		return brakes;
	}
	public void setBrakes(String brakes) {
		this.brakes = brakes;
	}
	public String getEmBrakes() {
		return emBrakes;
	}
	public void setEmBrakes(String emBrakes) {
		this.emBrakes = emBrakes;
	}
	public String getSpeedUp() {
		return speedUp;
	}
	public void setSpeedUp(String speedUp) {
		this.speedUp = speedUp;
	}
	public String getEmSpeedUp() {
		return emSpeedUp;
	}
	public void setEmSpeedUp(String emSpeedUp) {
		this.emSpeedUp = emSpeedUp;
	}
	public String getAverSpeed() {
		return averSpeed;
	}
	public void setAverSpeed(String averSpeed) {
		this.averSpeed = averSpeed;
	}
	public String getWaterTemp() {
		return waterTemp;
	}
	public void setWaterTemp(String waterTemp) {
		this.waterTemp = waterTemp;
	}
	public String getRpm() {
		return rpm;
	}
	public void setRpm(String rpm) {
		this.rpm = rpm;
	}
	public String getVoltage() {
		return voltage;
	}
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	public String getTotalFuelC() {
		return totalFuelC;
	}
	public void setTotalFuelC(String totalFuelC) {
		this.totalFuelC = totalFuelC;
	}
	public String getAverFuelC() {
		return averFuelC;
	}
	public void setAverFuelC(String averFuelC) {
		this.averFuelC = averFuelC;
	}
	public String getTiredTime() {
		return tiredTime;
	}
	public void setTiredTime(String tiredTime) {
		this.tiredTime = tiredTime;
	}
	public String getTravelID() {
		return travelID;
	}
	public void setTravelID(String travelID) {
		this.travelID = travelID;
	}

	public String getSposition() {
		return sposition;
	}

	public void setSposition(String position) {
		this.sposition = position;
	}

	public String getEposition() {
		return eposition;
	}

	public void setEposition(String eposition) {
		this.eposition = eposition;
	}

	public String getEqid() {
		return eqid;
	}

	public void setEqid(String eqid) {
		this.eqid = eqid;
	}


}
