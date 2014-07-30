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
			Bundle b = new Bundle();// �������
			b.putString("textviewContent", Integer.parseInt(content)+"��");
			msg.setData(b);
			handler.sendMessage(msg); // ��Handler������Ϣ,����UI
		}
		else{

			for(int i=0;i<=Integer.parseInt(content);i++){
				try {
					Message msg = new Message();
					msg.what = 2;
					Thread.sleep(20);
					Bundle b = new Bundle();// �������
					b.putString("textviewContent", i+"��");
					msg.setData(b);
					handler.sendMessage(msg); // ��Handler������Ϣ,����UI
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
