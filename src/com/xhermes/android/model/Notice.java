package com.xhermes.android.model;

public class Notice {
	private int id;
	private String eqid;
	private String title;
	private String content;
	private String sender;
	private String time;
	private int isRead;

	public Notice(){

	}

	public Notice(String notice){

	}

	public Notice(String title, String content, String date, String sender,String eqid) {
		this.title=title;
		this.content=content;
		this.time=date;
		this.eqid=eqid;
		this.sender=sender;
		this.isRead=0;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int isRead() {
		return isRead;
	}
	public void setRead(int i) {
		this.isRead = i;
	}

}
