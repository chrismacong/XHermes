package com.xhermes.android.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.htmlparser.util.ParserException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xhermes.android.R;
import com.xhermes.android.network.URLMaker;
import com.xhermes.android.util.GrabHtml;
import com.xhermes.android.util.Utilities;

public class VehicleDetailFragment extends Fragment{
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
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
	private ImageView imageView;
	private Handler handler; 
	String carName = "";
	private WindowManager w;
	private TextView from_sohu;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	public void onCreate(Bundle savedInstanceState) {
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
				signInResult = signInResult.trim();
				final String[] sign = signInResult.split(";");
				if(sign.length<=1){
					Utilities.showMessage(getActivity(), R.string.network_failed);
				}
				else{
					for(int i=0;i<sign.length;i++){
						list_content[i]=sign[i];
						if(i==4)
							carName = sign[i];
					}
				}

				for(int i =0; i < len; i++) { 
					Map<String,Object> item=new HashMap<String,Object>();
					item.put("title", list_title[i]);
					item.put("text", list_content[i]);
					item.put("image", list_imag[i]);
					vehicle_data.add(item);
				}
				adapter.notifyDataSetChanged();


				new Thread(new Runnable(){
					@Override
					public void run() {
						Message msg = new Message();
						try {
							Bundle data = new Bundle();
							carName = Utilities.toBrowserCode(carName, "gb2312");
							List<String> imageList = new GrabHtml(carName).parser();
							if(imageList.size()>0){
								String path  = imageList.get(0);
								Bitmap bitmap = returnBitMap(path);
								data.putParcelable("bitmap", bitmap);
								msg.setData(data);
								handler.sendMessage(msg);
							}
						} catch (ParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
		}.execute();
		super.onCreate(savedInstanceState);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View root=inflater.inflate(R.layout.vehicleinfo, null);
		//vehicle_listview=getListView();
		vehicle_listview=(ListView) root.findViewById(R.id.vel_list);
		imageView = (ImageView)root.findViewById(R.id.car_image);
		from_sohu = (TextView)root.findViewById(R.id.from_sohu);

		w = this.getActivity().getWindowManager();
		handler = new MyHandler();
		
		ctx=getActivity();
		adapter = new SimpleAdapter(getActivity(),vehicle_data,R.layout.vehicleinfo_detail,  
				new String[]{"image","title","text"},new int[]{R.id.image,R.id.title,R.id.text1});
		vehicle_listview.setAdapter(adapter);
		
		return root;
	}
	
	public Bitmap returnBitMap(String url){ 
		URL myFileUrl = null;   
		Bitmap bitmap = null;  
		try {   
			myFileUrl = new URL(url);   
		} catch (MalformedURLException e) {   
			e.printStackTrace();   
		}   
		try {   
			HttpURLConnection conn = (HttpURLConnection) myFileUrl   
					.openConnection();   
			conn.setDoInput(true);   
			conn.connect();   
			InputStream is = conn.getInputStream();   
			bitmap = BitmapFactory.decodeStream(is);   
			is.close();   
		} catch (IOException e) {   
			e.printStackTrace();   
		}   
		return bitmap;   
	}   

class MyHandler extends Handler {  

	public MyHandler() {  
		super();  
	}  

	public MyHandler(Looper looper) {  
		super(looper);  
	}  
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Bundle data = msg.getData();
		Bitmap bm = data.getParcelable("bitmap");
		String visible_str = data.getString("visibleOrNot");
		imageView.setVisibility(View.VISIBLE);
		from_sohu.setVisibility(View.VISIBLE);
		imageView.setImageBitmap(bm);
		int windowHeight = w.getDefaultDisplay().getHeight();
		int windowWidth = w.getDefaultDisplay().getWidth();
		int image_x = bm.getWidth();
		int image_y = bm.getHeight();
		int image_new_x = (int) (windowWidth*0.8>800?800:windowWidth*0.8);
		int image_new_y = (int)image_new_x * image_y/image_x;
		imageView.getLayoutParams().width = image_new_x;
		imageView.getLayoutParams().height = image_new_y;
	}
}
}
