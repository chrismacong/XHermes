package com.xhermes.android.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
	private RelativeLayout habit_ral_1;
	private RelativeLayout habit_ral_2;
	
	//achartengine
	private XYMultipleSeriesDataset mTimeDataset;
    private XYMultipleSeriesRenderer mTimeRenderer;
    private XYSeries mTimeCurrentSeries;
    private XYSeriesRenderer mTimeCurrentRenderer;
    private GraphicalView mTimeChartView;
    private LinearLayout timeLayout;
    private LinearLayout speedtimeLayout;
    

    private XYMultipleSeriesDataset mSpeedTimeDataset;
    private XYMultipleSeriesRenderer mSpeedTimeRenderer;
    private XYSeries mSpeedTimeCurrentSeries;
    private XYSeriesRenderer mSpeedTimeCurrentRenderer;
    private GraphicalView mSpeedTimeChartView;
    
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
		pd= new CustomProgressDialog(this.getActivity(), R.style.dialog,"正在拉取数据...");
		stamp1 = (ImageView)rootview.findViewById(R.id.stamp1);
		stamp2 = (ImageView)rootview.findViewById(R.id.stamp2);
		stamp3 = (ImageView)rootview.findViewById(R.id.stamp3);
		stamp4 = (ImageView)rootview.findViewById(R.id.stamp4);
		timeLayout = (LinearLayout) rootview.findViewById(R.id.time_layout);
		speedtimeLayout = (LinearLayout) rootview.findViewById(R.id.speedtime_layout);
		habit_ral_1 = (RelativeLayout)rootview.findViewById(R.id.habit_ral_1);
		habit_ral_2 = (RelativeLayout)rootview.findViewById(R.id.habit_ral_2);
		Calendar calendar = Calendar.getInstance();
		global_year = calendar.get(Calendar.YEAR);
		current_year = global_year;
		global_month = calendar.get(Calendar.MONTH);
		current_month = global_month;
//		if(global_month==0){
//			global_year--;
//			global_month = 11;
//		}
//		else
//			global_month--;
		global_day = calendar.get(Calendar.DAY_OF_MONTH);
		monthText.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1));
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
				if(global_month+global_year*12==current_month+current_year*12){
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
			if(this_year>current_year||(this_year==current_year&&this_monthOfYear>current_month)){
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
						String time_data[] = strs_split[14].split(",");
						//add line series
				        mTimeCurrentSeries = new XYSeries(getActivity().getResources().getString(R.string.habit_graph_time));
				        mTimeDataset.clear();
				        mTimeDataset.addSeries(mTimeCurrentSeries);
				        mTimeChartView = ChartFactory.getLineChartView(getActivity(), mTimeDataset, mTimeRenderer);
				        timeLayout.removeAllViews();
				        timeLayout.addView(mTimeChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				                ViewGroup.LayoutParams.MATCH_PARENT));
						int max_time = 0;
				        for(int i=0;i<24;i++){
				        	int time_in_that_hour = Integer.parseInt(time_data[i].trim());
				        	max_time = max_time>time_in_that_hour?max_time:time_in_that_hour;
				        	mTimeCurrentSeries.add(i, time_in_that_hour);
				        }
				        if(max_time<200)
				        	mTimeRenderer.setYAxisMax(200);
				        else if(max_time<500)
				        	mTimeRenderer.setYAxisMax(500);
				        else if(max_time<1000)
				        	mTimeRenderer.setYAxisMax(1000);
			        	if(mTimeChartView!=null)
			        		mTimeChartView.repaint();
			        	
			        	String speedtime_data[] = strs_split[15].split(",");
			        	//add line series
			        	mSpeedTimeCurrentSeries = new XYSeries(getActivity().getResources().getString(R.string.habit_graph_speedtime));
			        	mSpeedTimeDataset.clear();
			        	mSpeedTimeDataset.addSeries(mSpeedTimeCurrentSeries);
			        	mSpeedTimeChartView = ChartFactory.getLineChartView(getActivity(), mSpeedTimeDataset, mSpeedTimeRenderer);
			        	speedtimeLayout.removeAllViews();
			        	speedtimeLayout.addView(mSpeedTimeChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
			        			ViewGroup.LayoutParams.MATCH_PARENT));
			        	int max_speedtime = 0;
			        	for(int i=0;i<24;i++){
			        		int speedtime_in_that_hour = Integer.parseInt(speedtime_data[i].trim());
			        		max_speedtime = max_speedtime>speedtime_in_that_hour?max_speedtime:speedtime_in_that_hour;
			        		mSpeedTimeCurrentSeries.add(i, speedtime_in_that_hour);
			        	}
			        	if(max_speedtime<100)
			        		mSpeedTimeRenderer.setYAxisMax(100);
			        	if(mSpeedTimeChartView!=null)
			        		mSpeedTimeChartView.repaint();
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
		//当前月不进行规则判断
		if(global_year==current_year&&global_month==current_month)
			return stamps_list;
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
//				if(global_month==0){
//					dpd = new DatePickerDialog(getActivity(), new mDateSetListener(),
//						global_year-1,11,global_day);
//				}
//				else
//					dpd = new DatePickerDialog(getActivity(), new mDateSetListener(),
//							global_year,global_month-1,global_day);
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
		habit_ral_1.setVisibility(View.INVISIBLE);
		habit_ral_2.setVisibility(View.INVISIBLE);
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
				
				//achartengine
				mTimeDataset = new XYMultipleSeriesDataset();
		        mTimeRenderer = new XYMultipleSeriesRenderer();
				mTimeCurrentRenderer = new XYSeriesRenderer();
		        mTimeRenderer.addSeriesRenderer(mTimeCurrentRenderer);
		        setRendererStyle(mTimeRenderer, mTimeCurrentRenderer, getResources().getColor(R.color.time_series_color));
		        //set title for axis
		        mTimeRenderer.setXTitle(getActivity().getResources().getString(R.string.axis_x));
		        mTimeRenderer.setYTitle(getActivity().getResources().getString(R.string.axis_y1));
		        //set limit for axis
		        mTimeRenderer.setXAxisMax(24);
		        mTimeRenderer.setXAxisMin(0);
		        mTimeRenderer.setYAxisMax(2000);
		        mTimeRenderer.setYAxisMin(0);
		      //add line series
		        mTimeCurrentSeries = new XYSeries(getActivity().getResources().getString(R.string.habit_graph_time));
		        mTimeDataset.addSeries(mTimeCurrentSeries);
		        mTimeChartView = ChartFactory.getLineChartView(getActivity(), mTimeDataset, mTimeRenderer);
		        timeLayout.addView(mTimeChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
		                ViewGroup.LayoutParams.MATCH_PARENT));
				String time_data[] = strs_split[14].split(",");
				int max_time = 0;
		        for(int i=0;i<24;i++){
		        	int time_in_that_hour = Integer.parseInt(time_data[i].trim());
		        	max_time = max_time>time_in_that_hour?max_time:time_in_that_hour;
		        	mTimeCurrentSeries.add(i, time_in_that_hour);
		        }
		        if(max_time<200)
		        	mTimeRenderer.setYAxisMax(200);
		        else if(max_time<500)
		        	mTimeRenderer.setYAxisMax(500);
		        else if(max_time<1000)
		        	mTimeRenderer.setYAxisMax(1000);
	        	if(mTimeChartView!=null)
	        		mTimeChartView.repaint();
	        	//achartengine
	        	mSpeedTimeDataset = new XYMultipleSeriesDataset();
	        	mSpeedTimeRenderer = new XYMultipleSeriesRenderer();
	        	mSpeedTimeCurrentRenderer = new XYSeriesRenderer();
	        	mSpeedTimeRenderer.addSeriesRenderer(mSpeedTimeCurrentRenderer);
	        	setRendererStyle(mSpeedTimeRenderer, mSpeedTimeCurrentRenderer, getResources().getColor(R.color.speedtime_series_color));
	        	//set title for axis
	        	mSpeedTimeRenderer.setXTitle(getActivity().getResources().getString(R.string.axis_x));
	        	mSpeedTimeRenderer.setYTitle(getActivity().getResources().getString(R.string.axis_y2));
	        	//set limit for axis
	        	mSpeedTimeRenderer.setXAxisMax(24);
	        	mSpeedTimeRenderer.setXAxisMin(0);
	        	mSpeedTimeRenderer.setYAxisMax(200);
	        	mSpeedTimeRenderer.setYAxisMin(0);
	        	//add line series
	        	mSpeedTimeCurrentSeries = new XYSeries(getActivity().getResources().getString(R.string.habit_graph_speedtime));
	        	mSpeedTimeDataset.addSeries(mSpeedTimeCurrentSeries);
	        	mSpeedTimeChartView = ChartFactory.getLineChartView(getActivity(), mSpeedTimeDataset, mSpeedTimeRenderer);
	        	speedtimeLayout.addView(mSpeedTimeChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
	        			ViewGroup.LayoutParams.MATCH_PARENT));
	        	String speedtime_data[] = strs_split[15].split(",");
	        	int max_speedtime = 0;
	        	for(int i=0;i<24;i++){
	        		int speedtime_in_that_hour = Integer.parseInt(speedtime_data[i].trim());
	        		max_speedtime = max_speedtime>speedtime_in_that_hour?max_speedtime:speedtime_in_that_hour;
	        		mSpeedTimeCurrentSeries.add(i, speedtime_in_that_hour);
	        	}
	        	if(max_speedtime<100)
	        		mSpeedTimeRenderer.setYAxisMax(100);
	        	if(mSpeedTimeChartView!=null)
	        		mSpeedTimeChartView.repaint();
	        	habit_ral_1.setVisibility(View.VISIBLE);
	        	habit_ral_2.setVisibility(View.VISIBLE);
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
	private void setRendererStyle(XYMultipleSeriesRenderer renderer, XYSeriesRenderer currentRenderer, int color) {
        // set some properties on the main renderer
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(getResources().getColor(R.color.card_background));
        renderer.setAxisTitleTextSize(20);
        renderer.setAxesColor(getResources().getColor(R.color.axis_color));

        renderer.setMarginsColor(getResources().getColor(R.color.card_background));
        renderer.setXLabelsColor(getResources().getColor(R.color.axis_color));
        renderer.setYLabelsColor(0, getResources().getColor(R.color.axis_color));
        renderer.setInScroll(true);//in order to use in the scroll view

        renderer.setChartTitleTextSize(30);
        renderer.setLabelsTextSize(25);
        renderer.setLabelsColor(getResources().getColor(R.color.axis_color));
        renderer.setLegendTextSize(20);
        renderer.setMargins(new int[] { 30, 100, 30, 50});
        renderer.setZoomButtonsVisible(false);
        renderer.setPointSize(5);
        renderer.setPanEnabled(false, false);//block moving on both x and y side
        renderer.setZoomEnabled(false, false);//block zoom
        renderer.setXLabelsPadding(5);
        renderer.setYLabelsPadding(30);//set the padding between label and axis

        // set some properties on the current renderer
        currentRenderer.setPointStyle(PointStyle.CIRCLE);
        currentRenderer.setFillPoints(true);
        currentRenderer.setDisplayChartValues(true);
        currentRenderer.setColor(color);
        currentRenderer.setLineWidth(5);
        currentRenderer.setChartValuesTextSize(20);
        currentRenderer.setDisplayChartValuesDistance(10);
        currentRenderer.setShowLegendItem(false);
    }

}
