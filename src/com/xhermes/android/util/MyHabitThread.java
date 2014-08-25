package com.xhermes.android.util;

import android.os.Handler;
import android.os.Message;

public class MyHabitThread implements Runnable {
	private Handler handler; 
	public MyHabitThread(Handler handler){
		this.handler = handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Message habit_msg = new Message();
		habit_msg.what = 1;
		handler.sendMessage(habit_msg);
	}
}
