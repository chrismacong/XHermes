package com.xhermes.android.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class TravelInfo  implements Parcelable {
	public static final Parcelable.Creator<TravelInfo> CREATOR
	= new Parcelable.Creator<TravelInfo>() {
		public TravelInfo createFromParcel(Parcel in) {
			String data=in.readString();
			System.out.println("readString: "+data);
			String eqid=in.readString();
			int id=in.readInt();
			return new TravelInfo(data,eqid,id);
		}

		public TravelInfo[] newArray(int size) {
			return new TravelInfo[size];
		}
	};
	
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int arg1) {
			dest.writeString(this.toString());
			System.out.println("toString: "+this.toString());
			dest.writeString(eqid);
			dest.writeInt(id);
		}
	
		private int id;
		private String eqid;
		//开始截止时间
		private	String starttime;
		private String endtime;
		//距离
		private String distance;
		//最大速度
		private String maxSpeed;
		//超时时长
		private String timeOut;
		//刹车、紧急刹车
		private String brakes;
		private String emBrakes;
		//加速、紧急加速
		private String speedUp;
		private String emSpeedUp;
		//平均速度
		private String averSpeed;
		//发动机水温
		private String waterTemp;
		//发动机转速
		private String rpm;
		//电压
		private String voltage;
		//总油耗
		private String totalFuelC;
		//平均油耗
		private String averFuelC;
		//疲劳驾驶时间
		private String tiredTime;
		//行程序号
		private String travelID;
		//地理位置起点
		private String sposition;
		//地理位置终点
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
			System.out.println("string   "+this.toString());
		}
		
		 public TravelInfo(String readString, String eqid, int id) {
			this(readString);
			this.eqid=eqid;
			this.id=id;
		}

		public String toString() {
		        StringBuilder stringBuilder = new StringBuilder();
		        stringBuilder.append(endtime).append(";")
		                .append(starttime).append(";")
		                .append(distance).append(";")
		                .append(maxSpeed).append(";")
		                .append(timeOut).append(";")
		                .append(brakes).append(";")
		                .append(emBrakes).append(";")
		                .append(speedUp).append(";")
		                .append(emSpeedUp).append(";")
		                .append(averSpeed).append(";")
		                .append(waterTemp).append(";")
		                .append(rpm).append(";")
		                .append(voltage).append(";")
		                .append(totalFuelC).append(";")
		                .append(averFuelC).append(";")
		                .append(tiredTime).append(";")
		                .append(travelID).append(";")
		                .append(sposition).append(";")
		                .append(eposition);

		        return stringBuilder.toString();
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
