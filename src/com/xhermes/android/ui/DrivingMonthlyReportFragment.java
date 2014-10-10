package com.xhermes.android.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.xhermes.android.R;
import com.xhermes.android.network.URLMaker;
import com.xhermes.android.util.Utilities;

public class DrivingMonthlyReportFragment extends Fragment{
	private final String get_monthlytravelinfo_url = URLMaker.makeURL("mobile/mobilegetmonthlytravelinfobydate.html");
	private String terminalId;
	private TextView monthText;
	private PagerSlidingTabStrip tabs;
	private ProgressDialog pd;
	private ViewPager pager;
	private SwipeChartAdapter sca; 
	private int global_year;
	private int global_month;
	private int global_day;
	//private TextView testTextView;
	private static final int NUM_OF_PAGES = 7;
	private static final String[] DESCRIPTIONS = {"公里-日期","升-日期","次-日期","次-日期","分钟-日期","百公里升-日期","公里/小时-日期"};
	private String[] seperated_data_group;
	private ImageButton left_btn;
	private ImageButton right_btn;
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
		left_btn = (ImageButton) rootview.findViewById(R.id.button_chart_left);
		right_btn = (ImageButton) rootview.findViewById(R.id.button_chart_right);
		Calendar c = Calendar.getInstance();
		global_year = c.get(Calendar.YEAR);
		global_month = c.get(Calendar.MONTH);
		global_day = c.get(Calendar.DAY_OF_MONTH);
		pd= new CustomProgressDialog(this.getActivity(), R.style.dialog,"正在拉取数据...");
		pd.setCanceledOnTouchOutside(false);
		//testTextView = (TextView) rootview.findViewById(R.id.testTextView);
		pager = (ViewPager) rootview.findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) rootview.findViewById(R.id.tabs);
		monthText = (TextView)rootview.findViewById(R.id.chartTextView);
		Calendar calendar = Calendar.getInstance();
		monthText.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1));
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	private class SwipeChartAdapter extends FragmentPagerAdapter {
		private String[] titles;
		private ArrayList<Fragment> fragment_list;
		private FragmentManager fragmentManager;
		public SwipeChartAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
			this.fragmentManager = fragmentManager;
			fragment_list = new ArrayList<Fragment>();
			titles = new String[]{
					getString(R.string.tag_name_distance),
					getString(R.string.tag_name_fuel),
					getString(R.string.tag_name_brake),
					getString(R.string.tag_name_speedup),
					getString(R.string.tag_name_drivingtime),
					getString(R.string.tag_name_avgfuel),
					getString(R.string.tag_name_avgspeed)};
		}

		@Override
		public Fragment getItem(int position) {
			System.out.println(position);
			ChartFragment cf = new ChartFragment();
			Bundle bundle = new Bundle();  
			bundle.putString("terminalId", terminalId);
			bundle.putString("dataStr", seperated_data_group[position]);
			bundle.putString("description",DESCRIPTIONS[position]);
			cf.setArguments(bundle);
			fragment_list.add(cf);
			return cf;
		}

		@Override
		public int getCount() {
			return NUM_OF_PAGES;
		}

		@Override  
		public int getItemPosition(Object object) {  
			return POSITION_NONE;  
		}

		@Override
		public String getPageTitle(int position) {
			return titles[position];
		}
		public void resetFragments() {
			android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
			for(Fragment f:fragment_list){
				ft.remove(f);

			}
			ft.commit();
			ft=null;
			fragmentManager.executePendingTransactions();
			notifyDataSetChanged();
		}
	}
	private class mDateSetListener implements DatePickerDialog.OnDateSetListener  {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			monthText.setText(year + "-" + (monthOfYear + 1));
			final int this_year = year;
			final int this_monthOfYear = monthOfYear;
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
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					now.set(Calendar.YEAR, this_year);
					now.set(Calendar.MONTH,this_monthOfYear);
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
					//					System.out
					//					.println(signInResult);
					signInResult = signInResult.trim();
					seperated_data_group = signInResult.split("@");
					if(seperated_data_group==null){
						pd.dismiss();
						Utilities.showMessage(getActivity(), R.string.network_failed);
					}
					else if(seperated_data_group.length<=1){
						pd.dismiss();
						Utilities.showMessage(getActivity(), R.string.network_failed);
					}
					else{
						sca.resetFragments();
						pager.setAdapter(sca);
						pd.dismiss();
					}
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
				//				System.out
				//				.println(signInResult);
				signInResult = signInResult.trim();
				seperated_data_group = signInResult.split("@");
				if(seperated_data_group.length<=1){
					pd.dismiss();
					Utilities.showMessage(getActivity(), R.string.network_failed);
				}
				else{
					//				System.out.println(seperated_data_group[0]);
					sca = new SwipeChartAdapter(getChildFragmentManager());
					sca.resetFragments();
					pager.setAdapter(sca);
					tabs.setViewPager(pager);
					pd.dismiss();
				}
			}
		}.execute();
	}
}
