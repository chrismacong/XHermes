package com.xhermes.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.xhermes.android.R;
import com.xhermes.android.util.OverallFragmentController;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MessageFragment extends Fragment{
	private ArrayList<String> message_title =new ArrayList<String>();
	private ArrayList<String> message_content =new ArrayList<String>();
	private ArrayList<Object> message_image=new ArrayList<Object>();
	private ArrayList<String> message_time= new ArrayList<String>();
	ListView message_list;
	ArrayList<Map<String,Object>> message_data;
	private String terminalId;
	private Context ctx;
	private int id =0;
	private SimpleAdapter adapter;
	
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle b=getArguments();
		terminalId=b.getString("terminalId");
		message_data=new ArrayList<Map<String,Object>>();
		for(int i=0;i<6;i++){
			message_title.add("这是第"+i+"封邮件");
			message_content.add("这是第"+i+"封测试邮件");
			message_image.add(R.drawable.message);
			message_time.add("8月20日");
		}
		
		for(int i =0; i < message_title.size(); i++) { 
			Map<String,Object> item=new HashMap<String,Object>();
			item.put("title", message_title.get(i));
			item.put("text", message_content.get(i));
			item.put("image", message_image.get(i));
			item.put("time", message_time.get(i));	
			message_data.add(item);
		}
		
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		View root=inflater.inflate(R.layout.message_view, null);
		message_list=(ListView) root.findViewById(R.id.message_listview);
		ctx=getActivity();
		adapter = new SimpleAdapter(getActivity(),message_data,R.layout.message_listview,  
				new String[]{"image","title","text","time"},new int[]{R.id.mimage,R.id.mtitle,R.id.mtext,R.id.mtime});
		message_list.setAdapter(adapter);
		initView();
		return root;
	}
	
	private void initView() {
		message_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				MessageDetailFragment message_f=new MessageDetailFragment();
				Bundle b=new Bundle();
				b.putString("terminalId", terminalId);
				//b.putParcelable("message", travelInfoList.get(position));
				message_f.setArguments(b);
				FragmentManager fm=((FragmentActivity) ctx).getSupportFragmentManager();
				FragmentTransaction ft =fm.beginTransaction();
				ft.replace(R.id.fragment_container, message_f, "messagedetail");
				ft.commit();
				OverallFragmentController.removeFragment("messagedetail");
				OverallFragmentController.addFragment("messagedetail", message_f);

			}
		});
	}
	
}
