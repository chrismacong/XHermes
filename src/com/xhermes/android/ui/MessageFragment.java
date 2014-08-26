package com.xhermes.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xhermes.android.R;
import com.xhermes.android.dao.NoticeDao;
import com.xhermes.android.model.Notice;
import com.xhermes.android.util.OverallFragmentController;

public class MessageFragment extends Fragment{
	private ArrayList<String> message_title ;
	private ArrayList<String> message_content ;
	private ArrayList<Object> message_image;
	private ArrayList<String> message_time;
	ListView message_list;
	ArrayList<Map<String,Object>> message_data;
	private String terminalId;
	private Context ctx;
	private SimpleAdapter adapter;
	private static Handler mainActivityHandler;
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle b=getArguments();
		terminalId=b.getString("terminalId");
		message_data=new ArrayList<Map<String,Object>>();
		message_title =new ArrayList<String>();
		message_content =new ArrayList<String>();
		message_image=new ArrayList<Object>();
		message_time= new ArrayList<String>();
		NoticeDao ndao=new NoticeDao(ctx);
		List<Notice> nList=ndao.query(terminalId, "time desc", null);

		for(int i=0;i<nList.size();i++){
			message_title.add(nList.get(i).getTitle());
			message_content.add(nList.get(i).getContent());
			if(nList.get(i).isRead()!=1){
				message_image.add(R.drawable.message_unread);
			}else{
				message_image.add(R.drawable.message);
			}
			message_time.add(nList.get(i).getTime());
			
			Map<String,Object> item=new HashMap<String,Object>();
			item.put("id", nList.get(i).getId());
			item.put("title", nList.get(i).getTitle());
			item.put("text", nList.get(i).getContent());
			item.put("image", message_image.get(i));
			item.put("time",nList.get(i).getTime());	
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
				b.putInt("id",(Integer) message_data.get(position).get("id") );
				
				NoticeDao ndao=new NoticeDao(ctx);
				ndao.updateToRead((Integer)message_data.get(position).get("id"), 1);
				
				mainActivityHandler.sendEmptyMessage(0);
				
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

	public static Handler getMainActivityHandler() {
		return mainActivityHandler;
	}

	public static void setMainActivityHandler(Handler mainActivityHandler) {
		MessageFragment.mainActivityHandler = mainActivityHandler;
	}

}
