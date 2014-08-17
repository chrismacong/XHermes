package com.xhermes.android.model;

public class OBDParameters {
	private int id;
	private String eqid;
	private String current_miles;
	private String maintenance_gap;
	private String maintenance_next;
	private String time;

	public OBDParameters(String msg){
		String tmp[]=msg.split(";");
		current_miles=tmp[0];
		maintenance_gap=tmp[1];
		maintenance_next=tmp[2];
		time=tmp[3];
	}

	public OBDParameters() {
		// TODO Auto-generated constructor stub
	}

	public String getCurrent_miles() {
		return current_miles;
	}
	public void setCurrent_miles(String current_miles) {
		this.current_miles = current_miles;
	}
	public String getMaintenance_gap() {
		return maintenance_gap;
	}
	public void setMaintenance_gap(String maintenance_gap) {
		this.maintenance_gap = maintenance_gap;
	}
	public String getMaintenance_next() {
		return maintenance_next;
	}
	public void setMaintenance_next(String maintenance_next) {
		this.maintenance_next = maintenance_next;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEqid() {
		return eqid;
	}

	public void setEqid(String eqid) {
		this.eqid = eqid;
	}

}
