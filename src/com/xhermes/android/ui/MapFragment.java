package com.xhermes.android.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
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
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.xhermes.android.R;
import com.xhermes.android.dao.PositionDataDao;
import com.xhermes.android.model.PositionData;
import com.xhermes.android.network.DataReceiver;

public class MapFragment extends Fragment implements OnGetGeoCoderResultListener {
	private MapView mMapView;
	private RelativeLayout mapRelativeLayout;
	private BaiduMap mBaiduMap;
	private PositionDataDao pDao;
	private Marker carM;
	private String tid;
	private OverlayOptions o,ooPolyline;
	private Polyline lineOverlay;
	private ArrayList<LatLng> latlng;
	private ArrayList<Double> angList;
	private ArrayList<PositionData> dataList;
	private String latestDate;
	private LatLng desLatLng = null;
	private double agl;
	private Handler handler;
	private Context act;
	private int i=0;
	private int point=0;
	private ImageView carView;
	private boolean flag,isOver=false,isAdd=false,isFirst=true;
	private UiSettings mUiSettings;
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
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
		View rootview = inflater.inflate(R.layout.posiiton_view, container, false);
		mapRelativeLayout = (RelativeLayout)rootview.findViewById(R.id.map_relative_layout);
		carView = (ImageView)rootview.findViewById(R.id.bcarView);
		carView.setVisibility(View.GONE);
		mMapView = (MapView) rootview.findViewById(R.id.bmapView);  
		//		BaiduMapOptions opt=new BaiduMapOptions()
		//		.overlookingGesturesEnabled(false)
		//		.rotateGesturesEnabled(false)
		//		.mapStatus(new MapStatus.Builder().zoom(15).build());
		//		mMapView = new MapView(act, opt);
		mMapView.removeViewAt(1);
		mBaiduMap = mMapView.getMap();
		mMapView.setEnabled(false);
		MapStatus mapStatus = new MapStatus.Builder().zoom(15).build();
		MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);
		mBaiduMap.setMapStatus(msu);
		mUiSettings = mBaiduMap.getUiSettings();
		mUiSettings.setRotateGesturesEnabled(false);
		mUiSettings.setOverlookingGesturesEnabled(false);
		mUiSettings.setCompassEnabled(false);
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		set();

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				if(marker==carM){
					mSearch.reverseGeoCode(new ReverseGeoCodeOption()
					.location(desLatLng));
				}
				return true;
			}

		});

		/*MainActivity.MyOnTouchListener myOnTouchListener=new MainActivity.MyOnTouchListener() {

			int[] tempUp = new int[] { 0, 0 };
			int[] tempDown = new int[] { 0, 0 };
			@Override
			public boolean onTouch(MotionEvent ev) {
				// TODO Auto-generated method stub
				int action = ev.getAction();
				int x = (int) ev.getRawX();
				int y = (int) ev.getRawY();
				int p = (int) ev.getX();
				int q = (int) ev.getY();
				switch(action) {
				case MotionEvent.ACTION_DOWN:
					tempDown[0] = (int) ev.getX();
					tempDown[1] = (int) ev.getY();
					//					System.out.println("Down:" + tempDown[0] + " " + tempDown[1]);
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					tempUp[0] = (int) ev.getX();
					tempUp[1] = (int) ev.getY();
				}
				return false;
			}
		};
		((MainActivity)getActivity()).registerMyOnTouchListener(myOnTouchListener);
		mapRelativeLayout.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				int action = arg1.getAction();
				int x = (int) arg1.getRawX();
				int y = (int) arg1.getRawY();
				int p = (int) arg1.getX();
				int q = (int) arg1.getY();
				int[] temp = new int[] { 0, 0 };
				System.out.println("Touch");
				switch(action) {
				case MotionEvent.ACTION_DOWN:
					temp[0] = (int) arg1.getX();
					temp[1] = y - arg0.getTop();
					System.out.println("Down");
					break;
				case MotionEvent.ACTION_MOVE:
					int l = x - temp[0];
					int t = y - temp[1];
					System.out.println(l + " " + t);
				}
				return false;
			}

		});*/
		return rootview;
	}

	public void set(){
		UpdateData();
		UpdateMap();
		isOver=true;
	}

	public void refresh(){
		UpdateData();
		UpdateMap();
	}

	public void UpdateData(){
		point=i;
		dataList=pDao.query(tid,"time asc", i+",210");
		i+=dataList.size();
		if(dataList.size()>0)
			latestDate = "\n" + dataList.get(dataList.size()-1).getTime();
		Log.d("updateData"," point:"+point+" i:"+i);
		for(PositionData d:dataList){
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

	@SuppressLint("NewApi")
	public void UpdateMap(){
		if(desLatLng!=null){
			float zlevel=mBaiduMap.getMapStatus().zoom;
			if(latlng.size()>=2){
				LatLng olddesLatLng = latlng.get(latlng.size()-2);
				MapStatus oldmapsta=new MapStatus.Builder().target(olddesLatLng).zoom(zlevel).build();
				MapStatusUpdate oldupdateSta=MapStatusUpdateFactory.newMapStatus(oldmapsta);
				if(isOver){
					mBaiduMap.setMapStatus(oldupdateSta);
				}
			}
			animate();

			carM.setVisible(false);
			if (VERSION.SDK_INT >= 11 ) {
				carView.setRotation((float)-agl);
				carView.setVisibility(View.VISIBLE);
			}
			mUiSettings.setScrollGesturesEnabled(false);
			MapStatus mapsta=new MapStatus.Builder().target(desLatLng).zoom(zlevel).build();
			MapStatusUpdate updateSta=MapStatusUpdateFactory.newMapStatus(mapsta);
			if(isOver){
				mBaiduMap.animateMapStatus(updateSta,1500);
				final Handler h=new Handler();
				new Thread(){
					public void run(){
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						h.post(new Runnable(){
							public void run() {
								mUiSettings.setScrollGesturesEnabled(true);
								carView.setVisibility(View.GONE);
								carM.setVisible(true);
							}
						});

					}
				}.start();
			}
			else{
				mBaiduMap.setMapStatus(updateSta);
				mUiSettings.setScrollGesturesEnabled(true);
				carView.setVisibility(View.GONE);
				carM.setVisible(true);
			}
			mSearch.reverseGeoCode(new ReverseGeoCodeOption()
			.location(desLatLng));
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
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this.getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
			.show();
		}
		Toast.makeText(this.getActivity(), result.getAddress() + latestDate,
				Toast.LENGTH_LONG).show();
	}
}
