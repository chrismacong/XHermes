package com.xhermes.android.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MyThreadFor implements Runnable{
	private String content;
	private Handler handler; 
	public MyThreadFor(String content, Handler handler){
		this.content = content;
		this.handler = handler;
	}
	public void run() {
		if(OverallFragmentController.mainFragment_over_created){
			Message msg = new Message();
			msg.what = 2;
			Bundle b = new Bundle();// 存放数据
			b.putString("textviewContent", Integer.parseInt(content)+"分");
			msg.setData(b);
			handler.sendMessage(msg); // 向Handler发送消息,更新UI
		}
		else{

			for(int i=0;i<=Integer.parseInt(content);i++){
				try {
					Message msg = new Message();
					msg.what = 2;
					Thread.sleep(20);
					Bundle b = new Bundle();// 存放数据
					b.putString("textviewContent", i+"分");
					msg.setData(b);
					handler.sendMessage(msg); // 向Handler发送消息,更新UI
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
