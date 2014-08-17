package com.xhermes.android.network;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import cn.jpush.android.api.JPushInterface;

import com.xhermes.android.R;
import com.xhermes.android.dao.OBDDataDao;
import com.xhermes.android.dao.OBDParametersDao;
import com.xhermes.android.dao.PositionDataDao;
import com.xhermes.android.dao.TravelInfoDao;
import com.xhermes.android.model.OBDData;
import com.xhermes.android.model.OBDParameters;
import com.xhermes.android.model.PositionData;
import com.xhermes.android.model.TravelInfo;
import com.xhermes.android.ui.MainActivity;

/**
 * Receive data from the server and pass them to UI
 */
public class DataReceiver extends BroadcastReceiver {
	//    private static final String TAG = "DataReceiver";
	private static String eqid;
	private static Handler mapHandler;
	private static Handler obdHandler;
	public static Handler getObdHandler() {
		return obdHandler;
	}

	public static void setObdHandler(Handler obdHandler) {
		DataReceiver.obdHandler = obdHandler;
	}
	private static Activity act;
	private static int nid=0;
	private NotificationManager nm;
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

			if(title.contains("0003")){	//obdparameters
				String date = title.substring(title.indexOf("(")+1,title.indexOf(")"));
				message += ";" + date;
				System.out.println(message);
				OBDParameters data=new OBDParameters(message);
				data.setEqid(eqid);
				OBDParametersDao dao=new OBDParametersDao(context);
				boolean b = dao.insert(data);
				if(obdHandler!=null&&b)
					obdHandler.sendEmptyMessage(0);
			}
			if(title.contains("0004")){	//地理位置
				String date = title.substring(title.indexOf("(")+1,title.indexOf(")"));
				message += "," + date;
				String[] position=message.split(",");
				PositionData data=new PositionData(position);
				data.setEqid(eqid);
				PositionDataDao positionDao=new PositionDataDao(context);
				boolean b =positionDao.insert(data);
				if(mapHandler!=null&&b)
					mapHandler.sendEmptyMessage(0);
			}else if(title.contains("0007")){	//行程信息
				TravelInfo info=new TravelInfo(message);
				info.setEqid(eqid);
				TravelInfoDao infoDao=new TravelInfoDao(context);
				boolean b=infoDao.insert(info);
			}else if(title.contains("0008")){	//OBD数据
				String date = title.substring(title.indexOf("(")+1,title.indexOf(")"));
				message += ";" + date;
				OBDData data=new OBDData(message);
				data.setEqid(eqid);
				OBDDataDao dataDao=new OBDDataDao(context);
				boolean b = dataDao.insert(data);
				if(obdHandler!=null&&b)
					obdHandler.sendEmptyMessage(1);
			}else if(title.contains("Alert")){	//alert
				//Toast.makeText(context, message, Toast.LENGTH_LONG);
				if(message.indexOf("%%%")>-1)
					message = message.substring(0,message.lastIndexOf("%%%"));
				String alerts[] = message.split("%%%");
				String alertStr ="您的爱车出现了如下需要注意的问题：";
				for(int i=0;i<alerts.length;i++)
					alertStr += "\n"+i+"."+alerts[i];
				send(context.getResources().getString(R.string.newnotification),context.getResources().getString(R.string.newalert),alertStr);

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

	public void send(String tickerText,String title,String content){
		if(act==null)
			return;

		nid++;
		nm=(NotificationManager) act.getSystemService(Activity.NOTIFICATION_SERVICE);
		Intent intent=new Intent(act,MainActivity.class);
		PendingIntent pi=PendingIntent.getActivity(act, 0, intent, 0);

		RemoteViews contentView = new RemoteViews(act.getPackageName(), R.layout.notification_view);
		contentView.setImageViewResource(R.id.nimage, R.drawable.ic_launcher);
		contentView.setTextViewText(R.id.ntitle, title);
		contentView.setTextViewText(R.id.ntext, content);

		Notification n=new NotificationCompat.Builder(act)
		.setAutoCancel(true)
		.setContentIntent(pi)
		.setDefaults(Notification.DEFAULT_ALL)
		.setSmallIcon(R.drawable.message)
		.setTicker(tickerText)
		.setWhen(System.currentTimeMillis())
		.setContent(contentView)
		//.setContentInfo(Integer.toString(nid))
		.getNotification();

		nm.notify(nid, n);
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

	public static Activity getAct() {
		return act;
	}

	public static void setAct(Activity act) {
		DataReceiver.act = act;
	}
}
