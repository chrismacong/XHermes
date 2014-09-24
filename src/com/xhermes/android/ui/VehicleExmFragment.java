package com.xhermes.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xhermes.android.R;
import com.xhermes.android.network.URLMaker;
import com.xhermes.android.util.MyExmThread;
import com.xhermes.android.util.Utilities;

public class VehicleExmFragment extends Fragment{

	private MyProgress mp;
	private String terminalId;
	private ListView listview;
	private SimpleAdapter exmAdapter;
	private ArrayList<HashMap<String, Object>>   listItems; 
	private View rootview;
	private TextView check_text;
	private TextView check_text_english;
	private TextView mark_text;
	private ProgressBar progressBar;
	private Button start_exm_btn;
	private final String vehicle_exm_url = URLMaker.makeURL("mobile/vehicleexm.html");
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		terminalId = getArguments().getString("terminalId");
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview = inflater.inflate(R.layout.vehicleexm_view, container, false);
		listview = (ListView)rootview.findViewById(R.id.exmlist);
		check_text = (TextView)rootview.findViewById(R.id.check_text);
		check_text_english = (TextView)rootview.findViewById(R.id.check_text_english);
		mark_text = (TextView)rootview.findViewById(R.id.mark_text);
		progressBar = (ProgressBar)rootview.findViewById(R.id.circle_bar);
		start_exm_btn = (Button)rootview.findViewById(R.id.start_exm_btn);
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		progressBar.measure(w, h); 
		start_exm_btn.setWidth((int) (progressBar.getMeasuredWidth()*0.9));
		start_exm_btn.setHeight((int) (progressBar.getMeasuredHeight()*0.9));
		progressBar.setVisibility(View.INVISIBLE);
		mp = (MyProgress) rootview.findViewById(R.id.myprogress);
		mp.setProgress(0);
		
		/*-----------以下是放入button的onclick事件的代码---------*/
		start_exm_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				start_exm_btn.setEnabled(false);
				start_exm_btn.setVisibility(View.INVISIBLE);
				progressBar.setVisibility(View.VISIBLE);
				check_text.setText(R.string.check_state);
				check_text_english.setVisibility(View.VISIBLE);
				check_text_english.setText(R.string.check_state_english);
				mark_text.setText("100");
				mark_text.setTextSize(15);
				progressBar.setEnabled(false);
				initListView();
				listview.setAdapter(exmAdapter);
				addItem(R.drawable.exm_front_mini, "正在试图获取实时车辆数据", R.drawable.loading);
				
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
							result = com.xhermes.android.network.HttpClient.getInstance().httpPost(vehicle_exm_url,params);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return result;
					}

					@Override
					protected void onPostExecute(String signInResult) {
						signInResult = signInResult.trim();
						final String[] signs = signInResult.split(";");
						HashMap map = listItems.get(1);
						map.remove("exmlist_result_image");
						map.put("exmlist_result_image", R.drawable.pass);  
						String tempstr1 = "";
						String tempstr2 = "";
						String tempstr3 = "";
						if(signs.length<=1){
							Utilities.showMessage(getActivity(), R.string.network_failed);
						}
						else{
							if(signs[0].equals("0")){
								tempstr1 += "没有检测到体检数据，请稍后再试";
								HashMap<String, Object> tempMap = new HashMap<String, Object>(); 
								tempMap.put("exmlist_front_image", R.drawable.exm_front_mini);
								tempMap.put("exmlist_text", tempstr1);  
								tempMap.put("exmlist_result_image", R.drawable.pass);  
								listItems.add(tempMap);
						        listview.setAdapter(exmAdapter);
							}
							else{
								if(signs[1].equals("1")){
									tempstr1 += "非实时体检数据:" + signs[2];
								}
								else if(signs[1].equals("2")){
									tempstr1 += "实时体检数据:" + signs[2];
								}
								String[] faultcodes = signs[3].split(",");
								tempstr2 += "故障码:" + signs[3];
								tempstr3 += "进行故障分类";
								HashMap<String, Object> tempMap1 = new HashMap<String, Object>(); 
								tempMap1.put("exmlist_front_image", R.drawable.exm_front_mini);
								tempMap1.put("exmlist_text", tempstr1);  
								tempMap1.put("exmlist_result_image", R.drawable.pass); 
								HashMap<String, Object> tempMap2 = new HashMap<String, Object>(); 
								tempMap2.put("exmlist_front_image", R.drawable.exm_front_mini);
								tempMap2.put("exmlist_text", tempstr2);  
								tempMap2.put("exmlist_result_image", R.drawable.pass); 
								HashMap<String, Object> tempMap3 = new HashMap<String, Object>(); 
								tempMap3.put("exmlist_front_image", R.drawable.exm_front_mini);
								tempMap3.put("exmlist_text", tempstr3);  
								tempMap3.put("exmlist_result_image", R.drawable.loading); 
								listItems.add(tempMap1);
								listItems.add(tempMap2);
								mp.setProgress(10);
								listItems.add(tempMap3);
								listview.setAdapter(exmAdapter);
								MyHandler handler = new MyHandler();
								new Thread(new MyExmThread(signs[3], listItems, handler)).start();
							}
						}
					}
				}.execute();
			}
			
		});
		
		
		return rootview;
	}

	private void initListView()   {   
		listItems = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();   
		map.put("exmlist_front_image", R.drawable.exm_front_0);
		map.put("exmlist_text", "获取体检数据");  
		map.put("exmlist_result_image", R.drawable.loading);  
		listItems.add(map);   
		exmAdapter = new SimpleAdapter(this.getActivity(),listItems, 
				R.layout.item,
				new String[] {"exmlist_front_image", "exmlist_text", "exmlist_result_image"},     //动态数组与ImageItem对应的子项          
				new int[ ] {R.id.exmlist_front_image, R.id.exmlist_text, R.id.exmlist_result_image}      //list_item.xml布局文件里面的一个ImageView的ID,一个TextView 的ID   
				);   
	} 
	private void addItem(int frontR, String text, int BackR){
		 HashMap<String, Object> map = new HashMap<String, Object>();   
         map.put("exmlist_front_image", frontR);   
         map.put("exmlist_text", text);
         map.put("exmlist_result_image", BackR);
         listItems.add(map);
         listview.setAdapter(exmAdapter);
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
			//int args = msg.arg1;  
			//String s = (String)msg.obj;  

			//获取bundle对象的值  
			if (msg.what == 1) {  
				Bundle b = msg.getData();  
				int index = b.getInt("index"); 
				int exmlist_result_image = b.getInt("exmlist_result_image");
				int mp_int = b.getInt("mp");
				HashMap map = listItems.get(index);
				map.remove("exmlist_result_image");
				map.put("exmlist_result_image", exmlist_result_image);  
				listview.setAdapter(exmAdapter);
				mp.setProgress(mp_int);
			}
			
			else if(msg.what == 2) {
				Bundle b = msg.getData();  
				int exmlist_front_image = b.getInt("exmlist_front_image");
				String exmlist_text = b.getString("exmlist_text");
				int exmlist_result_image = b.getInt("exmlist_result_image");
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("exmlist_front_image", exmlist_front_image);
				map.put("exmlist_text", exmlist_text);  
				map.put("exmlist_result_image", exmlist_result_image); 
				listItems.add(map);
				listview.setAdapter(exmAdapter);
			}
			
			else if(msg.what == 3) {
				Bundle b = msg.getData();  
				int score = b.getInt("score");
				String main_solution = b.getString("main_solution");
				int mp_int = b.getInt("mp");
				mp.setProgress(mp_int);
				check_text.setText(main_solution);
				check_text_english.setText("");
				check_text_english.setVisibility(View.GONE);
				progressBar.setEnabled(true);
				mark_text.setText(score+"");
				start_exm_btn.setBackgroundResource(R.drawable.exm_finish_btn);
				start_exm_btn.setEnabled(true);
				progressBar.setVisibility(View.INVISIBLE);
				start_exm_btn.setVisibility(View.VISIBLE);
				mark_text.setTextSize(25);
			}
			
			else if(msg.what == 4) {
				Bundle b = msg.getData();  
				int doing_index = b.getInt("doing_index");
				ImageView imageView = (ImageView)rootview.findViewById(doing_index);
				imageView.setImageResource(R.drawable.done);
			}
			
			else if(msg.what == 5) {
				Bundle b = msg.getData();  
				int exmlist_front_image = b.getInt("exmlist_front_image");
				String exmlist_text = b.getString("exmlist_text");
				int exmlist_result_image = b.getInt("exmlist_result_image");
				int score = b.getInt("score");
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("exmlist_front_image", exmlist_front_image);
				map.put("exmlist_text", exmlist_text);  
				map.put("exmlist_result_image", exmlist_result_image); 
				listItems.add(map);
				listview.setAdapter(exmAdapter);
				mark_text.setText(score+"");
			}
			//System.out.println("handler线程ID:"+Thread.currentThread().getId());  
		}  
	}
}
