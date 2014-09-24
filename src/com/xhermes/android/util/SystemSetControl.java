package com.xhermes.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SystemSetControl {
	private SharedPreferences preference;
	private Editor editor;

	private boolean isSaved;
	private boolean isAutoLog;
	private boolean isBeep;
	private boolean isShock;
	private boolean isNoticeReceive;

	public SystemSetControl(Context ctx){
		preference=ctx.getSharedPreferences("Settings",ctx.MODE_PRIVATE);
		editor=preference.edit();
	}

	public boolean isShock() {
		isShock=preference.getBoolean("isShock", true);
		return isShock;
	}

	public void setShock(boolean isShock) {
		editor.putBoolean("isShock", isShock);
		editor.commit();
		this.isShock = isShock;
	}

	public boolean isNoticeReceive() {
		isNoticeReceive=preference.getBoolean("isNoticeReceive", true);
		return isNoticeReceive;
	}

	public void setNoticeReceive(boolean isNoticeReceive) {
		editor.putBoolean("isNoticeReceive", isNoticeReceive);
		editor.commit();
		this.isNoticeReceive = isNoticeReceive;
	}

	public boolean isBeep() {
		isBeep=preference.getBoolean("isBeep", true);
		return isBeep;
	}

	public void setBeep(boolean isBeep) {
		editor.putBoolean("isBeep", isBeep);
		editor.commit();
		this.isBeep = isBeep;
	}

	public boolean isAutoLog() {
		isAutoLog=preference.getBoolean("isAutoLog", false);
		return isAutoLog;
	}

	public void setAutoLog(boolean isAutoLog) {
		editor.putBoolean("isAutoLog", isAutoLog);
		editor.commit();
		this.isAutoLog = isAutoLog;
	}

	public boolean isSaved() {
		isSaved=preference.getBoolean("isSaved", false);
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		editor.putBoolean("isSaved", isSaved);
		editor.commit();
		this.isSaved = isSaved;
	}

	public void saveUser(String username,String pwd){
		editor.putBoolean("isSaved", true);
		editor.putString("username", username);
		editor.putString("pwd", pwd);
		editor.commit();
	}

	public String getUsername(){
		return preference.getString("username", "");
	}
	
	public String getPwd(){
		return preference.getString("pwd", "");
	}
}
