package com.xhermes.android.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xhermes.android.R;
import com.xhermes.android.network.URLMaker;
import com.xhermes.android.util.MyHabitThread;

public class DrivingHabitFragment extends Fragment{
	private final String get_monthlytravelexm_url = URLMaker.makeURL("mobile/mobilegetmonthlytravelexmbydate.html");
	private String terminalId;
	private TextView habitmonthly_title;
	//private TextView testTextView;
	private int global_year;
	private int global_month;
	private SimpleAdapter habitAdapter;
	private int global_day;
	private ImageButton left_btn;
	private ImageButton right_btn;
	private TextView monthText;
	private ArrayList<HashMap<String, Object>>   listItems; 
	private ListView listview;
	private ProgressDialog pd;
	private ImageView stamp1;
	private ImageView stamp2;
	private ImageView stamp3;
	private ImageView stamp4;
	private int current_year;
	private int current_month;
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		terminalId = getArguments().getString("terminalId");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootview = inflater.inflate(R.layout.drivinghabit_view, container, false);
		//testTextView = (TextView) rootview.findViewById(R.id.testTextView_report);
		habitmonthly_title = (TextView) rootview.findViewById(R.id.habitmonthly_title);
		left_btn = (ImageButton) rootview.findViewById(R.id.habit_button_chart_left);
		right_btn = (ImageButton) rootview.findViewById(R.id.habit_button_chart_right);
		monthText = (TextView)rootview.findViewById(R.id.habit_chartTextView);
		listview = (ListView)rootview.findViewById(R.id.habitlist);
		pd= new CustomProgressDialog(this.getActivity(), R.style.dialog,"");
		stamp1 = (ImageView)rootview.findViewById(R.id.stamp1);
		stamp2 = (ImageView)rootview.findViewById(R.id.stamp2);
		stamp3 = (ImageView)rootview.findViewById(R.id.stamp3);
		stamp4 = (ImageView)rootview.findViewById(R.id.stamp4);
		Calendar calendar = Calendar.getInstance();
		global_year = calendar.get(Calendar.YEAR);
		current_year = global_year;
		global_month = calendar.get(Calendar.MONTH);
		current_month = global_month;
		if(global_month==0){
			global_year--;
			global_month = 11;
		}
		else
			global_month--;
		global_day = calendar.get(Calendar.DAY_OF_MONTH);
		monthText.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)));
		monthText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Calendar c = Calendar.getInstance();
				DatePickerDialog datePickerDialog = createDialogWithoutDateField();
				datePickerDialog.show();

			}
		});
		left_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(global_month==1){
					global_year--;
					global_month = 12;
				}
				else
					global_month--;
				new mDateSetListener().onDateSet(null, global_year, global_month, global_day);
			}

		});
		right_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(global_month==current_month&&global_year==current_year){
					Toast.makeText(getActivity(), "日期超出范围",
							Toast.LENGTH_SHORT).show();
				}
				else{
					if(global_month==12){
						global_year++;
						global_month = 1;
					}
					else
						global_month++;
					new mDateSetListener().onDateSet(null, global_year, global_month, global_day);
				}
			}

		});
		return rootview;
	}
	private class mDateSetListener implements DatePickerDialog.OnDateSetListener  {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			final int this_year = year;
			final int this_monthOfYear = monthOfYear;
			if(this_year>current_year||(this_year==current_year&&this_monthOfYear>=current_month)){
				Toast.makeText(getActivity(), "日期超出范围",
						Toast.LENGTH_SHORT).show();
			}
			else{
				monthText.setText(year + "-" + (monthOfYear + 1));
				global_year = year;
				global_month = monthOfYear;
				pd.show();
				new AsyncTask<Void, Void, String>() {
					@Override
					protected void onPreExecute() {

					}

					@Override
					protected String doInBackground(Void... voids) {
						String result = "";
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("terminalId", terminalId));
						Calendar now = Calendar.getInstance();
						params.add(new BasicNameValuePair("year", global_year+""));
						params.add(new BasicNameValuePair("month", global_month +""));
						try {
							result = com.xhermes.android.network.HttpClient.getInstance().httpPost(get_monthlytravelexm_url, params);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return result;
					}

					@Override
					protected void onPostExecute(String signInResult) {
						System.out
						.println(signInResult);

						/*-----------Test code below-------------*/
						String[] strs_split = signInResult.split("@");
						for(int i=0;i<strs_split.length;i++){
							if(strs_split[i]==null||("null").equals(strs_split[i]))
								strs_split[i]="0";
						}
						/*String temp = "";
					temp += "总距离：" + strs_split[0] + "\n";
					temp += "最大距离：" + strs_split[1] + "\n";
					temp += "最大速度：" + strs_split[2] + "\n";
					temp += "总超时时长：" + strs_split[3] + "\n";
					temp += "总急刹车次数：" + strs_split[4] + "\n";
					temp += "总紧急刹车次数：" + strs_split[5] + "\n";
					temp += "总急加速次数：" + strs_split[6] + "\n";
					temp += "总紧急加速次数：" + strs_split[7] + "\n";
					temp += "平均速度：" + strs_split[8] + "\n";
					temp += "发动机最高水温：" + strs_split[9] + "\n";
					temp += "发动机最高转速：" + strs_split[10] + "\n";
					temp += "总油耗：" + strs_split[11] + "\n";
					temp += "平均油耗：" + strs_split[12] + "\n";
					temp += "总疲劳驾驶时间：" + strs_split[13] + "\n";
					temp += "驾驶时间数组：" + strs_split[14] + "\n";
					temp += "驾驶时间和平均速度关系数组：" + strs_split[15];
					testTextView.setText(temp);*/
						/*-----------Test code above-------------*/

						initListView(strs_split);
						listview.setAdapter(habitAdapter);
						habitmonthly_title.setText(global_year + "年" + (global_month + 1) + "月驾驶习惯报表");
						showStamps(getStamps(strs_split));
						MyHandler handler = new MyHandler();
						new Thread(new MyHabitThread(handler)).start();
						pd.dismiss();
					}
				}.execute();
			}
		}
	}

	private void initListView(String[] habits)   {   
		listItems = new ArrayList<HashMap<String, Object>>(); 
		int[] habitIndexs = {R.string.habit_name_1, 
				R.string.habit_name_2,
				R.string.habit_name_3,
				R.string.habit_name_4,
				R.string.habit_name_5,
				R.string.habit_name_6,
				R.string.habit_name_7,
				R.string.habit_name_8,
				R.string.habit_name_9,
				R.string.habit_name_10,
				R.string.habit_name_11,
				R.string.habit_name_12,
				R.string.habit_name_13,
				R.string.habit_name_14
		};
		//add first 14 set of parameter
		for(int i=0; i<habitIndexs.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();   
			map.put("habitlist_front_image", R.drawable.habit_front_mini);
			map.put("habitlist_name", getActivity().getResources().getString(habitIndexs[i]));  
			//如果小数位数多余2两位则取两位小数，判断写的比较长
			map.put("habitlist_data", habits[i].contains(".")?(habits[i].substring(habits[i].indexOf(".")).length()>2?habits[i].substring(0,habits[i].indexOf(".")+3):habits[i]):habits[i]);  
			listItems.add(map);
		}
		//add parameter of time in total
		HashMap<String, Object> map = new HashMap<String, Object>();  
		map.put("habitlist_front_image", R.drawable.habit_front_mini);
		map.put("habitlist_name", getActivity().getResources().getString(R.string.habit_name_15));
		int time_in_total = 0;
		for(String index_time:habits[14].split(","))
			time_in_total += Integer.parseInt(index_time);
		map.put("habitlist_data", time_in_total);
		listItems.add(map);
		habitAdapter = new SimpleAdapter(this.getActivity(),listItems, 
				R.layout.habititem,
				new String[] {"habitlist_front_image", "habitlist_name", "habitlist_data"},     //动态数组与ImageItem对应的子项          
				new int[ ] {R.id.habitlist_front_image, R.id.habitlist_name, R.id.habitlist_data}      //list_item.xml布局文件里面的一个ImageView的ID,一个TextView 的ID   
				);   
	} 

	//规则判断
	private ArrayList<Integer> getStamps(String[] habits){
		ArrayList<Integer> stamps_list = new ArrayList<Integer>();
		//distance
		int distance = Integer.parseInt(habits[0]);
		if(distance<100)
			stamps_list.add(R.drawable.distance_stamp_1);
		else if(distance>=1000&&distance<3000)
			stamps_list.add(R.drawable.distance_stamp_2);
		else if(distance>=3000)
			stamps_list.add(R.drawable.distance_stamp_3);

		//fuel
		double fuel = Double.parseDouble(habits[12]);
		if(distance>100){
			if(fuel>13)
				stamps_list.add(R.drawable.fuel_stamp_1);
			else if(fuel<10&&fuel>8)
				stamps_list.add(R.drawable.fuel_stamp_2);
			else if(fuel<=8)
				stamps_list.add(R.drawable.fuel_stamp_3);
		}

		//stable
		int stable = 0;
		stable += Integer.parseInt(habits[4]);
		stable += Integer.parseInt(habits[5]) * 2;
		stable += Integer.parseInt(habits[6]);
		stable += Integer.parseInt(habits[7]) * 2;
		if(distance>100){
			if(stable>1000)
				stamps_list.add(R.drawable.stable_stamp_1);
			else if(stable<=200&&stable>50)
				stamps_list.add(R.drawable.stable_stamp_2);
			else if(stable<=50)
				stamps_list.add(R.drawable.stable_stamp_3);
		}

		//tired time
		int tired_time = Integer.parseInt(habits[13]);
		if(distance>100){
			if(tired_time>1000)
				stamps_list.add(R.drawable.tired_stamp_1);
			else if(tired_time<=100&&tired_time>0)
				stamps_list.add(R.drawable.tired_stamp_2);
			else if(tired_time==0)
				stamps_list.add(R.drawable.tired_stamp_3);
		}

		return stamps_list;
	}
	//显示勋章
	private void showStamps(ArrayList<Integer> list){
		ImageView[] views = {stamp1, stamp2, stamp3, stamp4};
		for(int i=0;i<views.length;i++){
			views[i].setImageDrawable(null);
		}
		for(int i=0;i<list.size();i++){
			Drawable d = getActivity().getResources().getDrawable(list.get(i));
			d.setAlpha(150);
			views[i].setImageDrawable(d);
		}
	}
	private DatePickerDialog createDialogWithoutDateField(){
		DatePickerDialog dpd = new DatePickerDialog(getActivity(), new mDateSetListener(),
				global_year ,global_month ,global_day);
				if(global_month==0){
					dpd = new DatePickerDialog(getActivity(), new mDateSetListener(),
						global_year-1,11,global_day);
				}
				else
					dpd = new DatePickerDialog(getActivity(), new mDateSetListener(),
							global_year,global_month-1,global_day);
		try{
			java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
			for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
				if (datePickerDialogField.getName().equals("mDatePicker")) {
					datePickerDialogField.setAccessible(true);
					DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
					java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
					for (java.lang.reflect.Field datePickerField : datePickerFields) {
						if ("mDaySpinner".equals(datePickerField.getName())) {
							datePickerField.setAccessible(true);
							Object dayPicker = new Object();
							dayPicker = datePickerField.get(datePicker);
							((View) dayPicker).setVisibility(View.GONE);
						}
					}
				}

			}
		}catch(Exception ex){
		}
		return dpd;

	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		pd.show();
		new AsyncTask<Void, Void, String>() {
			@Override
			protected void onPreExecute() {

			}

			@Override
			protected String doInBackground(Void... voids) {
				String result = "";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("terminalId", terminalId));
				Calendar now = Calendar.getInstance();
				params.add(new BasicNameValuePair("year", global_year+""));
				params.add(new BasicNameValuePair("month", global_month +""));
				try {
					result = com.xhermes.android.network.HttpClient.getInstance().httpPost(get_monthlytravelexm_url, params);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(String signInResult) {
				System.out
				.println(signInResult);
				signInResult = signInResult.trim();

				/*-----------Test code below-------------*/
				String[] strs_split = signInResult.split("@");
				/*String temp = "";
				temp += "总距离：" + strs_split[0] + "\n";
				temp += "最大距离：" + strs_split[1] + "\n";
				temp += "最大速度：" + strs_split[2] + "\n";
				temp += "总超时时长：" + strs_split[3] + "\n";
				temp += "总急刹车次数：" + strs_split[4] + "\n";
				temp += "总紧急刹车次数：" + strs_split[5] + "\n";
				temp += "总急加速次数：" + strs_split[6] + "\n";
				temp += "总紧急加速次数：" + strs_split[7] + "\n";
				temp += "平均速度：" + strs_split[8] + "\n";
				temp += "发动机最高水温：" + strs_split[9] + "\n";
				temp += "发动机最高转速：" + strs_split[10] + "\n";
				temp += "总油耗：" + strs_split[11] + "\n";
				temp += "平均油耗：" + strs_split[12] + "\n";
				temp += "总疲劳驾驶时间：" + strs_split[13] + "\n";
				temp += "驾驶时间数组：" + strs_split[14] + "\n";
				temp += "驾驶时间和平均速度关系数组：" + strs_split[15];
				testTextView.setText(temp);*/
				/*-----------Test code above-------------*/
				initListView(strs_split);
				listview.setAdapter(habitAdapter);
				habitmonthly_title.setText(global_year + "年" + (global_month + 1) + "月驾驶习惯报表");
				showStamps(getStamps(strs_split));
				pd.dismiss();
			}
		}.execute();
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
				listview.setAdapter(habitAdapter);
			}
		}  
	}

}
