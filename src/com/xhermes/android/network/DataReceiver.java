package com.xhermes.android.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.xhermes.android.dao.OBDDataDao;
import com.xhermes.android.dao.PositionDataDao;
import com.xhermes.android.dao.TravelInfoDao;
import com.xhermes.android.model.OBDData;
import com.xhermes.android.model.PositionData;
import com.xhermes.android.model.TravelInfo;

/**
 * Receive data from the server and pass them to UI
 */
public class DataReceiver extends BroadcastReceiver {
	//    private static final String TAG = "DataReceiver";
	private static String eqid;
	private static Handler mapHandler;
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.i(bundle.getString(JPushInterface.EXTRA_TITLE),bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// 自定义消息不会展示在通知栏，完全要开发者写代码去处理
			String title=bundle.getString(JPushInterface.EXTRA_TITLE);
			String message=bundle.getString(JPushInterface.EXTRA_MESSAGE);
			if(title.contains("0004")){	//地理位置
				String date = title.substring(title.indexOf("(")+1,title.indexOf(")"));
				message += "," + date;
				String[] position=message.split(",");
				PositionData data=new PositionData(position);
				data.setEqid(eqid);
				PositionDataDao positionDao=new PositionDataDao(context);
				boolean b =positionDao.insert(data);
				if(mapHandler!=null)
					mapHandler.sendEmptyMessage(0);
			}else if(title.contains("0007")){	//行程信息
				TravelInfo info=new TravelInfo(message);
				info.setEqid(eqid);
				TravelInfoDao infoDao=new TravelInfoDao(context);
				boolean b=infoDao.insert(info);
			}else if(title.contains("0008")){	//OBD数据
				OBDData data=new OBDData(message);
				data.setEqid(eqid);
				OBDDataDao dataDao=new OBDDataDao(context);
				dataDao.insert(data);
			}else if(title.contains("Alert")){	//alert
				Toast.makeText(context, message, Toast.LENGTH_LONG);
			}
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			System.out.println("收到了通知");
			// 在这里可以做些统计，或者做些其他工作
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			System.out.println("用户点击打开了通知");
			String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String extra = (String) bundle.get(JPushInterface.EXTRA_EXTRA); 
			// 在这里可以自己写代码去定义用户点击后的行为
		} else {
			Log.d("abc", "Unhandled intent - " + intent.getAction());
		}
	}
	public static String getEqid() {
		return eqid;
	}
	public static void setEqid(String eqid) {
		DataReceiver.eqid = eqid;
	}
	public static Handler getMapHandler() {
		return mapHandler;
	}
	public static void setMapHandler(Handler mapHandler) {
		DataReceiver.mapHandler = mapHandler;
	}
}
