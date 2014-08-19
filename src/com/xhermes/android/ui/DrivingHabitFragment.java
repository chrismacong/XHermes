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

public class DrivingHabitFragment extends Fragment{
	private final String get_monthlytravelinfo_url = URLMaker.makeURL("mobile/mobilegetmonthlytravelinfobydate.html");
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
		View rootview = inflater.inflate(R.layout.drivinghabit_view, container, false);
		testTextView = (TextView) rootview.findViewById(R.id.testTextView);
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
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = df.format(now.getTime());
				params.add(new BasicNameValuePair("date", date));
				try {
					result = com.xhermes.android.network.HttpClient.getInstance().httpPost(get_monthlytravelinfo_url, params);
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
				testTextView.setText(signInResult);
			}
		}.execute();
		return rootview;
	}
}
