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
				temp += "�ܾ��룺" + strs_split[0] + "\n";
				temp += "�����룺" + strs_split[1] + "\n";
				temp += "����ٶȣ�" + strs_split[2] + "\n";
				temp += "�ܳ�ʱʱ����" + strs_split[3] + "\n";
				temp += "�ܼ�ɲ��������" + strs_split[4] + "\n";
				temp += "�ܽ���ɲ��������" + strs_split[5] + "\n";
				temp += "�ܼ����ٴ�����" + strs_split[6] + "\n";
				temp += "�ܽ������ٴ�����" + strs_split[7] + "\n";
				temp += "ƽ���ٶȣ�" + strs_split[8] + "\n";
				temp += "���������ˮ�£�" + strs_split[9] + "\n";
				temp += "���������ת�٣�" + strs_split[10] + "\n";
				temp += "���ͺģ�" + strs_split[11] + "\n";
				temp += "ƽ���ͺģ�" + strs_split[12] + "\n";
				temp += "��ƣ�ͼ�ʻʱ�䣺" + strs_split[13] + "\n";
				temp += "��ʻʱ�����飺" + strs_split[14] + "\n";
				temp += "��ʻʱ���ƽ���ٶȹ�ϵ���飺" + strs_split[15];
				testTextView.setText(temp);
				/*-----------Test code above-------------*/
			}
		}.execute();
		return rootview;
	}
}
