package com.xhermes.android.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.xhermes.android.R;
import com.xhermes.android.dao.PositionDataDao;
import com.xhermes.android.model.PositionData;
import com.xhermes.android.network.DataReceiver;

public class MapFragment extends Fragment{
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private PositionDataDao pDao;
	private Marker carM;
	private String tid;
	private OverlayOptions o,ooPolyline;
	private Polyline lineOverlay;
	private ArrayList<LatLng> latlng;
	private ArrayList<Double> angList;
	private ArrayList<PositionData> dataList;
	private LatLng desLatLng = null;
	private double agl;
	private Handler handler;
	private Context act;
	private int i=0;
	private int point=0;
	private boolean flag,isOver=false,isAdd=false,isFirst=true;
	BitmapDescriptor caricon=BitmapDescriptorFactory.fromResource(R.drawable.cartest);

	class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				refresh();
			}
		}
	}

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Bundle bundle=getArguments();
		tid=bundle.getString("terminalId");
		Log.d("tid",tid);
		act=getActivity();
		pDao =new PositionDataDao(act);
		handler=new MyHandler();
		DataReceiver.setMapHandler(handler);
		o = new MarkerOptions().icon(caricon).zIndex(5).anchor(0.5f, 0.5f);
		ooPolyline= new PolylineOptions().width(10).color(0xAAFF0000);
		angList=new ArrayList<Double>();
		latlng=new ArrayList<LatLng>();
		dataList=new ArrayList<PositionData>();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		BaiduMapOptions opt=new BaiduMapOptions()
		.overlookingGesturesEnabled(false)
		.rotateGesturesEnabled(false)
		.mapStatus(new MapStatus.Builder().zoom(15).build());
		mMapView = new MapView(act, opt);
		mMapView.removeViewAt(1);
		mBaiduMap = mMapView.getMap();
		mMapView.setEnabled(false);
		set();
		return mMapView;
	}

	public void set(){
		UpdateData();
		UpdateMap();
		isOver=true;
	}

	public void refresh(){
		UpdateData();
		UpdateMap();
		//		final Handler h=new Handler();
		//		new Thread(){
		//			public void run(){
		//				flag=true;
		//				while(flag){
		//					h.post(new Runnable(){
		//						public void run() {
		//							if(point<i){
		//								desLatLng=latlng.get(point);
		//								Log.d("refresh","move to "+desLatLng);
		//								UpdateMap();
		//								point++;
		//								Log.d("thread"," point:"+point+" i:"+i);
		//							}else
		//								flag=false;
		//						}
		//					});
		//
		//					try {
		//						Thread.sleep(500);
		//					} catch (InterruptedException e) {
		//						e.printStackTrace();
		//					}
		//				}
		//			}
		//		}.start();
	}

	public void UpdateData(){
		point=i;
		dataList=pDao.query(tid,"time asc", i+",210");
		i+=dataList.size();
		Log.d("updateData"," point:"+point+" i:"+i);
		for(PositionData d:dataList){
			System.out.println(d.getTime());
			ConvertMapCoord(d);
		}
	}

	public void animate(){
		if(desLatLng!=null){
			if(isFirst){
				//mo=new MarkerOverlay(act);
				((MarkerOptions) o).position(desLatLng);
				carM=(Marker) mBaiduMap.addOverlay(o);
				carM.setRotate((float)agl);
				isFirst=false;
				return;
			}
			carM.setRotate((float)agl);

			//			Projection pro=mBaiduMap.getProjection();
			//			Point startP=pro.toScreenLocation(desLatLng);
			//			carM.setVisible(false);
			//			mo.setP(startP);
			//			mMapView.addView(mo, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			//			mo.setVisibility(View.VISIBLE);

			//			Point endP=pro.toScreenLocation(desLatLng);
			//			int dx=endP.x-startP.x;
			//			int dy=endP.y-startP.y;
			//			double s=dy/dx;
			//			int dur=1000;
			//			for(int i=0;i<dur;i++){
			//				double px=endP.x+dx*i/dur;
			//				double py=endP.y+dy*i/dur;
			//				Point pt=new Point();
			//				pt.set((int)px, (int)py);
			//				LatLng temp=pro.fromScreenLocation(pt);
			//				carM.setPosition(temp);
			//			}
			//TranslateAnimation ta=new TranslateAnimation(startP.x,startP.y,endP.x,endP.y);
			//ta.setDuration(1000);
			//o.setAnimate(ta);
		}
	}

	public void UpdateMap(){
		if(desLatLng!=null){
			animate();

			float zlevel=mBaiduMap.getMapStatus().zoom;
			MapStatus mapsta=new MapStatus.Builder().target(desLatLng).zoom(zlevel).build();
			MapStatusUpdate updateSta=MapStatusUpdateFactory.newMapStatus(mapsta);

			if(isOver)
				mBaiduMap.animateMapStatus(updateSta,1500);
			else
				mBaiduMap.setMapStatus(updateSta);

			if(latlng.size()>=2){
				if(isAdd)
					lineOverlay.setPoints(latlng);
				else{
					((PolylineOptions) ooPolyline).points(latlng);
					lineOverlay = (Polyline) mBaiduMap.addOverlay(ooPolyline);
					isAdd=true;
				}
			}
			carM.setPosition(desLatLng);
			carM.setRotate((float)agl);
			//mo.setVisibility(View.VISIBLE);
			//carM.setVisible(true);
		}
	}

	public void ConvertMapCoord(PositionData d){
		double lat=Double.parseDouble(d.getLat());
		double lon=Double.parseDouble(d.getLon());
		LatLng p = new LatLng(lat,lon);
		// 将gps坐标转换成百度坐标  
		CoordinateConverter converter  = new CoordinateConverter();  
		converter.from(CoordType.GPS);  
		// p待转换坐标  
		converter.coord(p);  
		desLatLng = converter.convert();  
		latlng.add(desLatLng);
		if(latlng.size()>20){
			int size=latlng.size();
			for(int ri=0;i<size-20;i++){
				latlng.remove(0);
			}
		}
		Log.d("laton",""+lat+";"+lon);
		String angle=d.getAngle().replace("°", "");
		agl= Double.parseDouble(angle);
		angList.add(agl);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView.onDestroy();
		caricon.recycle();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
		refresh();
	}
}
