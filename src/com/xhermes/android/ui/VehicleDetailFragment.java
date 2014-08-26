package com.xhermes.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.xhermes.android.R;
import com.xhermes.android.network.URLMaker;

public class VehicleDetailFragment extends Fragment{
	@Override
	public void onDestroy() {
		System.out.println("fragment onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		System.out.println("fragment onDetach");
		super.onDetach();
	}

	@Override
	public void onPause() {
		System.out.println("fragment onPauseh");
		super.onPause();
	}

	@Override
	public void onResume() {
		System.out.println("fragment onResume");
		super.onResume();
	}

	@Override
	public void onStart() {
		System.out.println("fragment onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		System.out.println("fragment onStop");
		super.onStop();
	}

	private String[] list_title= {"设备 ID","邮箱","车主昵称","车牌号","车型","联系电话"};
	private String[] list_content=new String[6];
	private Object[] list_imag={R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5,R.drawable.p6};
	int len=list_title.length;
	private final String vehicleinfo_url = URLMaker.makeURL("mobile/mobilegetterminalinfo.html");
	ListView vehicle_listview;
	ArrayList<Map<String,Object>> vehicle_data;
	private String terminalId;
	private Context ctx;
	private int id =0;
	private SimpleAdapter adapter;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	public void onCreate(Bundle savedInstanceState) {
		System.out.println("fragment oncreate");
		// TODO Auto-generated method stub
		Bundle b=getArguments();
		terminalId=b.getString("terminalId");
		vehicle_data=new ArrayList<Map<String,Object>>();
		
		new AsyncTask<Void, Void, String>() {
			@Override
			protected void onPreExecute() {
			}

			@Override
			protected String doInBackground(Void... voids) {
				String result = "";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("terminalId", terminalId));
				try {
					result = com.xhermes.android.network.HttpClient.getInstance().httpPost(vehicleinfo_url, params);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(String signInResult) {
				System.out.println(signInResult);
				signInResult = signInResult.trim();
				System.out.println(signInResult);
				final String[] sign = signInResult.split(";");
				for(int i=0;i<sign.length;i++){
					list_content[i]=sign[i];
				}
				
				for(int i =0; i < len; i++) { 
					Map<String,Object> item=new HashMap<String,Object>();
					item.put("title", list_title[i]);
					item.put("text", list_content[i]);
					item.put("image", list_imag[i]);
					vehicle_data.add(item);
				}
				adapter.notifyDataSetChanged();
			}
		}.execute();
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		System.out.println("fragment oncreateView");
		View root=inflater.inflate(R.layout.vehicleinfo, null);
		//vehicle_listview=getListView();
		vehicle_listview=(ListView) root.findViewById(R.id.vel_list);
	
		ctx=getActivity();
		adapter = new SimpleAdapter(getActivity(),vehicle_data,R.layout.vehicleinfo_detail,  
				new String[]{"image","title","text"},new int[]{R.id.image,R.id.title,R.id.text1});
		vehicle_listview.setAdapter(adapter);
		return root;
	}
}
