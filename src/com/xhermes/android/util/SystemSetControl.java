package com.xhermes.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SystemSetControl {
	private SharedPreferences preference;
	private Editor editor;
	
	private boolean isBeep;
	private boolean isShock;
	private boolean isNoticeReceive;
	
	public SystemSetControl(Context ctx){
		preference=ctx.getSharedPreferences("Settings",ctx.MODE_PRIVATE);
		editor=preference.edit();
	}
	
	public boolean isShock() {
		isShock=preference.getBoolean("isShcok", true);
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
}
