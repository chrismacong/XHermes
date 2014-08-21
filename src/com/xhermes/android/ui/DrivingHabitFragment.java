package com.xhermes.android.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xhermes.android.R;
import com.xhermes.android.network.URLMaker;

public class DrivingHabitFragment extends Fragment{
	private final String get_monthlytravelexm_url = URLMaker.makeURL("mobile/mobilegetmonthlytravelexmbydate.html");
	private String terminalId;
	private TextView habitmonthly_title;
	private TextView testTextView;
	private int global_year;
	private int global_month;
	private int global_day;
	private ImageButton left_btn;
	private ImageButton right_btn;
	private TextView monthText;
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
		testTextView = (TextView) rootview.findViewById(R.id.testTextView_report);
		habitmonthly_title = (TextView) rootview.findViewById(R.id.habitmonthly_title);
		left_btn = (ImageButton) rootview.findViewById(R.id.habit_button_chart_left);
		right_btn = (ImageButton) rootview.findViewById(R.id.habit_button_chart_right);
		monthText = (TextView)rootview.findViewById(R.id.habit_chartTextView);
		Calendar calendar = Calendar.getInstance();
		global_year = calendar.get(Calendar.YEAR);
		global_month = calendar.get(Calendar.MONTH);
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
				if(global_month==12){
					global_year++;
					global_month = 1;
				}
				else
					global_month++;
				new mDateSetListener().onDateSet(null, global_year, global_month, global_day);
			}
			
		});
		return rootview;
	}
	private class mDateSetListener implements DatePickerDialog.OnDateSetListener  {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			monthText.setText(year + "-" + (monthOfYear + 1));
			final int this_year = year;
			final int this_monthOfYear = monthOfYear;
			global_year = year;
			global_month = monthOfYear;
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
					String temp = "";
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
					testTextView.setText(temp);
					/*-----------Test code above-------------*/

					habitmonthly_title.setText(global_year + "年" + (global_month + 1) + "月驾驶习惯报表");
				}
			}.execute();
		}
	}

	private DatePickerDialog createDialogWithoutDateField(){
		DatePickerDialog dpd = new DatePickerDialog(getActivity(), new mDateSetListener(),
				global_year,global_month,global_day);
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
				String temp = "";
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
				testTextView.setText(temp);
				/*-----------Test code above-------------*/

				habitmonthly_title.setText(global_year + "年" + global_month + "月驾驶习惯报表");
			}
		}.execute();
	}
}
