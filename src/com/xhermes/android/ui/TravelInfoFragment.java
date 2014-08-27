package com.xhermes.android.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xhermes.android.R;
import com.xhermes.android.dao.TravelInfoDao;
import com.xhermes.android.model.TravelInfo;
import com.xhermes.android.network.URLMaker;
import com.xhermes.android.util.DateController;
import com.xhermes.android.util.OverallFragmentController;

public class TravelInfoFragment extends Fragment{

	private String terminalId;
	private Context ctx;
	private Button start_btn,end_btn,date_btn,reset_btn;
	private int year, monthOfYear, dayOfMonth, hourOfDay, minute,tyear,tmonth,tday;
	private ImageView left_img,right_img;
	private ListView travelinfo_listView;
	private ProgressDialog pd;
	private ArrayList<TravelInfo> travelInfoList;
	private TravelInfoDao travelDao;
	private String starttime;
	private String endtime;
	private String sdate,edate;
	private final String get_travelinfo_url = URLMaker.makeURL("mobile/mobilegettravelinfo.html");
	private TravelInfoAdapter travel_adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		terminalId = getArguments().getString("terminalId");
		ctx=getActivity();

		year = DateController.getYear();
		monthOfYear = DateController.getMonthOfYear();
		dayOfMonth = DateController.getDayOfMonth();
		starttime = this.getTime(DateController.getStart_hourOfDay(), DateController.getStart_minute());
		endtime = this.getTime(DateController.getEnd_hourOfDay(), DateController.getEnd_minute());

		tyear=year;
		tmonth=monthOfYear;
		tday=dayOfMonth;

		String date=String.valueOf(year).substring(2) + String.format("%02d", (monthOfYear + 1)) +  String.format("%02d",dayOfMonth);
		sdate=date+this.getTime(DateController.getStart_hourOfDay(), DateController.getStart_minute()) + "00";
		edate=date+this.getTime(DateController.getEnd_hourOfDay(), DateController.getEnd_minute()) + "00";
		travelDao=new TravelInfoDao(ctx);
		travelInfoList=new ArrayList<TravelInfo>();
		
		pd= new CustomProgressDialog(ctx,R.style.dialog,"");
		pd.setCanceledOnTouchOutside(false);
		getTravelInfo(sdate,edate);
		//travelInfoList=travelDao.queryByDate(terminalId, "starttime asc", "", sdate, edate);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.travelinfo_view, container, false);
		start_btn=(Button) rootview.findViewById(R.id.starttime_btn);
		end_btn=(Button) rootview.findViewById(R.id.endtime_btn);
		date_btn=(Button) rootview.findViewById(R.id.date_btn);
		reset_btn=(Button) rootview.findViewById(R.id.reset_btn);

		left_img=(ImageView) rootview.findViewById(R.id.left_img);
		right_img=(ImageView) rootview.findViewById(R.id.right_img);

		travelinfo_listView=(ListView) rootview.findViewById(R.id.travelinfo_list_View);
		initView();
		return rootview;
	}

	public void dataRefresh(){
		sdate=getStartDateFromFormat();
		edate=getEndDateFromFormat();
		//		System.out.println(sdate+"    "+edate);
		//ArrayList<TravelInfo> tmpList=travelDao.queryByDate(terminalId, "starttime asc", "", sdate, edate);
		
		getTravelInfo(sdate, edate);
		travel_adapter.notifyDataSetChanged();
	}

	public String getStartDateFromFormat(){
		String date=(String) date_btn.getText();
		date=date.substring(2);
		date=date.replace("-", "");
		String stime=(String) start_btn.getText();
		stime=stime.replace(":", "");
		return date+stime+"00";
	}

	public String getEndDateFromFormat(){
		String date=(String) date_btn.getText();
		date=date.substring(2);
		date=date.replace("-", "");
		String etime=(String) end_btn.getText();
		etime=etime.replace(":", "");
		return date+etime+"00";
	}

	public String getDate( int year, int monthOfYear,int dayOfMonth){
		return year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" +  String.format("%02d",dayOfMonth);
	}

	public String getTime(int arg1,int arg2){
		return String.format("%02d", arg1)+":"+String.format("%02d", arg2);
	}
	
	public String getTimeInFormat(String time_Str){
		return time_Str.substring(0,2) + "-" + 
				time_Str.substring(2,4) + "-" + 
				time_Str.substring(4,6) + " " + 
				time_Str.substring(6,8) + ":" + 
				time_Str.substring(8,10) + ":" + 
				time_Str.substring(10,12);
	}

	private void getTravelInfo(final String sdate,final String edate){
		
		pd.show();
		
		new AsyncTask<Void, Void, String>() {
			ArrayList<TravelInfo> tempList=new ArrayList<TravelInfo>();
			@Override
			protected void onPreExecute() {
				
			}

			@Override
			protected String doInBackground(Void... voids) {
				String result = "";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("terminalId", terminalId));
				params.add(new BasicNameValuePair("from_time_point", sdate));
				params.add(new BasicNameValuePair("to_time_point", edate));
				try {
					result = com.xhermes.android.network.HttpClient.getInstance().httpPost(get_travelinfo_url,params);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(String signInResult) {
				signInResult = signInResult.trim();
				System.out.println("execute:" + signInResult);
				String[] stringList=signInResult.split("@");
				String terminalId=stringList[0];
				travelInfoList.clear();
				if(stringList.length>1){
					for(int i=1;i<stringList.length;i++){
						TravelInfo t=new TravelInfo(stringList[i]);
						travelInfoList.add(t);
					}
				}
				travel_adapter.notifyDataSetChanged();
				pd.dismiss();
				System.out.println("dismiss");
			}
		}.execute();
	}
	private void initView() {
		date_btn.setText(getDate(year,monthOfYear,dayOfMonth));
		start_btn.setText(starttime);
		end_btn.setText(endtime);

		travel_adapter=new TravelInfoAdapter(travelInfoList, (Activity) ctx);
		travelinfo_listView.setAdapter(travel_adapter);

		travelinfo_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				TravelInfoDetailFragment tdf=new TravelInfoDetailFragment();
				Bundle b=new Bundle();
				b.putString("terminalId", terminalId);
				b.putParcelable("travelinfo", travelInfoList.get(position));
				tdf.setArguments(b);
				FragmentManager fm=((FragmentActivity) ctx).getSupportFragmentManager();
				FragmentTransaction ft =fm.beginTransaction();
				ft.replace(R.id.fragment_container, tdf, "travel_info_detail_fragment");
				ft.commit();
				OverallFragmentController.removeFragment("travel_info_detail_fragment");
				OverallFragmentController.addFragment("travel_info_detail_fragment", tdf);

			}
		});

		start_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TimePickerDialog datePickerDialog = new TimePickerDialog(ctx, new OnTimeSetListener()
				{
					@Override
					public void onTimeSet(TimePicker v, int arg1, int arg2) {
						start_btn.setText(getTime(arg1,arg2));
						DateController.setStart_hourOfDay(arg1);
						DateController.setStart_minute(arg2);
						dataRefresh();
					}
				}, DateController.getStart_hourOfDay(), DateController.getStart_minute(), true);
				datePickerDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,  
						WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				datePickerDialog.show();				
			}
		});

		end_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TimePickerDialog datePickerDialog = new TimePickerDialog(ctx, new OnTimeSetListener()
				{
					@Override
					public void onTimeSet(TimePicker v, int arg1, int arg2) {
						end_btn.setText(getTime(arg1,arg2));
						DateController.setEnd_hourOfDay(arg1);
						DateController.setEnd_minute(arg2);
						dataRefresh();
					}
				}, DateController.getEnd_hourOfDay(), DateController.getEnd_minute(), true);
				datePickerDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,  
						WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				datePickerDialog.show();				
			}
		});

		date_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener()
				{
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth){
						tyear=year;
						tmonth=monthOfYear;
						tday=dayOfMonth;
						date_btn.setText(getDate(year,monthOfYear,dayOfMonth));
						DateController.setYear(tyear);
						DateController.setMonthOfYear(tmonth);
						DateController.setDayOfMonth(tday);
						dataRefresh();
					}
				}, year, monthOfYear, dayOfMonth);
				datePickerDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,  
						WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				datePickerDialog.show();				
			}
		});

		reset_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DateController.init();
				starttime = getTime(DateController.getStart_hourOfDay(), DateController.getStart_minute());
				endtime = getTime(DateController.getEnd_hourOfDay(), DateController.getEnd_minute());
				tyear = DateController.getYear();
				tmonth = DateController.getMonthOfYear();
				tday = DateController.getDayOfMonth();
				date_btn.setText(getDate(tyear,tmonth,tday));
				start_btn.setText(starttime);
				end_btn.setText(endtime);
				dataRefresh();
			}
		});

		left_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, tyear);
				calendar.set(Calendar.MONTH, tmonth);
				calendar.set(Calendar.DAY_OF_MONTH, tday);
				if(calendar.get(Calendar.DAY_OF_YEAR)==1)
					calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)-1);
				calendar.roll(Calendar.DAY_OF_YEAR,-1);
				tyear = calendar.get(Calendar.YEAR);
				tmonth = calendar.get(Calendar.MONTH);
				tday = calendar.get(Calendar.DAY_OF_MONTH);
				DateController.setYear(tyear);
				DateController.setMonthOfYear(tmonth);
				DateController.setDayOfMonth(tday);
//				tmonth-=1;
//				if(tmonth<0){
//					tmonth=11;
//					tyear-=1;
//				}
				date_btn.setText(getDate(tyear,tmonth,tday));
				dataRefresh();
			}
		});

		right_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, tyear);
				calendar.set(Calendar.MONTH, tmonth);
				calendar.set(Calendar.DAY_OF_MONTH, tday);
				calendar.roll(Calendar.DAY_OF_YEAR,1);
				if(calendar.get(Calendar.DAY_OF_YEAR)==1)
					calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+1);
				tyear = calendar.get(Calendar.YEAR);
				tmonth = calendar.get(Calendar.MONTH);
				tday = calendar.get(Calendar.DAY_OF_MONTH);
				DateController.setYear(tyear);
				DateController.setMonthOfYear(tmonth);
				DateController.setDayOfMonth(tday);
//				tmonth+=1;
//				if(tmonth>11){
//					tmonth=0;
//					tyear+=1;
//				}
				date_btn.setText(getDate(tyear,tmonth,tday));
				dataRefresh();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
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
		// TODO Auto-generated method stub
		super.onResume();
	}

	private class TravelInfoAdapter extends BaseAdapter{

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

		private int position;
		private ArrayList<TravelInfo> contents;
		private LayoutInflater inflater;
		private ImageView travelinfo_image_view,travelinfo_map_view;
		private TextView travelinfo_startt_textview,travelinfo_endt_textview,travelinfo_startp_textview,travelinfo_endp_textview;
		private View touchLayout;
		private Activity ctx;
		DisplayMetrics metric ;

		public TravelInfoAdapter(ArrayList<TravelInfo> travelInfoList,Activity ctx){
			this.ctx=ctx;
			inflater=ctx.getLayoutInflater();
			contents=travelInfoList;
			metric= new DisplayMetrics();
			ctx.getWindowManager().getDefaultDisplay().getMetrics(metric);
		}

		@Override
		public int getCount() {
			return contents.size();
		}

		@Override
		public Object getItem(int arg0) {
			return contents.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return position;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			View root=inflater.inflate(R.layout.travelinfo_list_item_view, null);
			travelinfo_image_view=(ImageView) root.findViewById(R.id.travelinfo_image_view);
			travelinfo_startt_textview=(TextView) root.findViewById(R.id.travelinfo_startt_textview);
			travelinfo_endt_textview=(TextView) root.findViewById(R.id.travelinfo_endt_textview);
			travelinfo_startp_textview=(TextView) root.findViewById(R.id.travelinfo_startp_textview);
			travelinfo_endp_textview=(TextView) root.findViewById(R.id.travelinfo_endp_textview);

			String starttime=contents.get(position).getStarttime();
			String startp=contents.get(position).getSposition();

			String endtime=contents.get(position).getEndtime();
			String endp=contents.get(position).getEposition();

			int s=3;
			int sp=startp.length();
			int ep=endp.length();

			travelinfo_startt_textview.setText(getTimeInFormat(starttime));
			travelinfo_endt_textview.setText(getTimeInFormat(endtime));
			travelinfo_startp_textview.setText(startp);
			travelinfo_endp_textview.setText(endp);

			travelinfo_startt_textview.setWidth(getWidth()*2/3);
			travelinfo_startp_textview.setWidth(getWidth()*2/3);
			travelinfo_endt_textview.setWidth(getWidth()*2/3);
			travelinfo_endp_textview.setWidth(getWidth()*2/3);

			travelinfo_image_view.setMinimumWidth(getWidth()/3);
			return root;
		}

		public int getWidth(){
			int width = metric.widthPixels; // ÆÁÄ»¿í¶È£¨ÏñËØ£©
			int height = metric.heightPixels; // ÆÁÄ»¸ß¶È£¨ÏñËØ£©
			float density = metric.density; // ÆÁÄ»ÃÜ¶È£¨0.75 / 1.0 / 1.5£©
			int densityDpi = metric.densityDpi; // ÆÁÄ»ÃÜ¶ÈDPI£¨120 / 160 / 240£©
			return width;
		}

		public float getDensity(){
			float density = metric.density; // ÆÁÄ»ÃÜ¶È£¨0.75 / 1.0 / 1.5£©
			return density;
		}

		public SpannableString textFormat(String s,int i,int j){
			SpannableString ss=new SpannableString(s);
			ForegroundColorSpan fcst = new ForegroundColorSpan(Color.rgb(0, 0, 0)); 
			ForegroundColorSpan fcsq = new ForegroundColorSpan(Color.rgb(127, 230, 223));
			StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
			ss.setSpan(fcst, 0, i, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			ss.setSpan(new RelativeSizeSpan(1.2f), 0, i, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			ss.setSpan(fcsq, i, j, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			ss.setSpan(new RelativeSizeSpan(0.8f), i, j, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			return ss;
		}
	}

}
