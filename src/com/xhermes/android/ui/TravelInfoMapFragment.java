package com.xhermes.android.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.xhermes.android.model.PositionData;

public class TravelInfoMapFragment extends Fragment{
	private String terminalId;
	private Activity ctx;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private OverlayOptions ooPolyline;
	private Polyline lineOverlay;
	private ArrayList<LatLng> latlngList;
	private ArrayList<Double> angleList;
	private String positionList;
	private LatLng desLatLng;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b=getArguments();
		terminalId=b.getString("terminalId");
		positionList=b.getString("positionList");
		ctx=getActivity();
		latlngList=new ArrayList<LatLng>();
		angleList=new ArrayList<Double>();
		
		String[] list=positionList.split("@");
		System.out.println(positionList);
		if(positionList!=null&&!positionList.equals("")){
			for(String position:list){
				String[] data=position.split(";");
				PositionData pdata=new PositionData(data);
				latlngList.add(convertToLatlng(pdata));
			}
			desLatLng=latlngList.get(latlngList.size()-1);
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		mMapView = new MapView(ctx);
		MapStatus mapStatus;
		if(desLatLng!=null)
			mapStatus=new MapStatus.Builder().zoom(15).target(desLatLng).build();
		else
			mapStatus=new MapStatus.Builder().zoom(15).build();
		
		mMapView = new MapView(ctx,new BaiduMapOptions().mapStatus(
				new MapStatus.Builder().target(desLatLng).zoom(15).build()));
		mBaiduMap=mMapView.getMap();
		if(latlngList.size()>1){
			ooPolyline= new PolylineOptions().width(10).color(0xAAFF0000).points(latlngList);
			mBaiduMap.addOverlay(ooPolyline);
		}
		return mMapView;
	}
	
	private LatLng convertToLatlng(PositionData d){
		double lat=Double.parseDouble(d.getLat());
		double lon=Double.parseDouble(d.getLon());
		LatLng p = new LatLng(lat,lon);
		// 将gps坐标转换成百度坐标  
		CoordinateConverter converter  = new CoordinateConverter();  
		converter.from(CoordType.GPS);  
		// p待转换坐标  
		converter.coord(p);  
		p = converter.convert();
		//方向角处理
//		String angle=d.getAngle().replace("°", "");
//		double agl= Double.parseDouble(angle);
//		angleList.add(agl);
		return p;
	}
}
