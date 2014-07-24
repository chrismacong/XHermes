package com.xhermes.android.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MyThread implements Runnable {
	private String content;
	private Handler handler; 
	public MyThread(String content, Handler handler){
		this.content = content;
		this.handler = handler;
	}
	public void run() {
		String[] contents = content.split(";");
		String str = "";
		for(int i=0;i<contents.length;i++){
			try {
				Message msg = new Message();
				msg.what = 1;
				int index = (int) (Math.random()*500);
				Thread.sleep(index);
				str += contents[i] + "\n";
				Bundle b = new Bundle();// 存放数据
				b.putString("textviewContent", str);
				msg.setData(b);
				handler.sendMessage(msg); // 向Handler发送消息,更新UI
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}