package com.xhermes.android.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhermes.android.R;
import com.xhermes.android.network.URLMaker;

public class DrivingMonthlyReportFragment extends Fragment{
	private final String get_monthlytravelexm_url = URLMaker.makeURL("mobile/mobilegetmonthlytravelexmbydate.html");
	private String terminalId;
	private TextView testTextView;
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		terminalId = getArguments().getString("terminalId");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootview = inflater.inflate(R.layout.drivingmonthlyreport_view, container, false);
		testTextView = (TextView) rootview.findViewById(R.id.testTextView_report);
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
				String year = now.get(Calendar.YEAR) + "";
				String month = now.get(Calendar.MONTH) + "";
				params.add(new BasicNameValuePair("year", year));
				params.add(new BasicNameValuePair("month", month));
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
			}
		}.execute();
		return rootview;
	}
}
