package com.xhermes.android.util;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// ��ʹ�� SDK �����֮ǰ��ʼ�� context ��Ϣ������ ApplicationContext
		SDKInitializer.initialize(this);
	}

}
