package com.xhermes.android.ui;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.MapView;
import com.xhermes.android.R;
import com.xhermes.android.dao.PositionDataDao;
import com.xhermes.android.model.PositionData;

public class PositionInfoFragment  extends Fragment{
	private String terminalId;
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView.onDestroy();  
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

	private MapView mMapView;
	private PositionDataDao positionDataDao;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootview = inflater.inflate(R.layout.posiiton_view, container, false);
		mMapView = (MapView) rootview.findViewById(R.id.bmapView);  
		positionDataDao = new PositionDataDao(getActivity());
		List<PositionData> p_list = positionDataDao.query(terminalId, "time desc", "10");
		System.out.println(p_list.size());
		for(int i=0;i<p_list.size();i++){
			System.out.println(p_list.get(i).getLat() + "," + p_list.get(i).getLon() + "," + p_list.get(i).getAngle());
		}
		return rootview;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		terminalId = getArguments().getString("terminalId");
	}
	
}
