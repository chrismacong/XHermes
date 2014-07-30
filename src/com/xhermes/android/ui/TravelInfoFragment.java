package com.xhermes.android.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.graphics.Color;
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
import com.xhermes.android.dao.PositionDataDao;
import com.xhermes.android.dao.TravelInfoDao;
import com.xhermes.android.model.TravelInfo;

public class TravelInfoFragment extends Fragment{

	private String terminalId;
	private Context ctx;
	private Button start_btn,end_btn,date_btn,reset_btn;
	private int year, monthOfYear, dayOfMonth, hourOfDay, minute,tyear,tmonth,tday;
	private ImageView left_img,right_img;
	private ListView travelinfo_listView;
	private ArrayList<TravelInfo> travelInfoList;
	private TravelInfoDao travelDao;
	private String starttime="00:00";
	private String endtime="23:59";
	private String sdate,edate;
	private TravelInfoAdapter travel_adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		terminalId = getArguments().getString("terminalId");
		ctx=getActivity();

		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		monthOfYear = calendar.get(Calendar.MONTH);
		dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);

		tyear=year;
		tmonth=monthOfYear;
		tday=dayOfMonth;
		
		String date=String.valueOf(year).substring(2) + String.format("%02d", (monthOfYear + 1)) +  String.format("%02d",dayOfMonth);
		sdate=date+"000000";
		edate=date+"235900";
		travelDao=new TravelInfoDao(ctx);
		travelInfoList=travelDao.queryByDate(terminalId, "starttime asc", "", sdate, edate);
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
		ArrayList<TravelInfo> tmpList=travelDao.queryByDate(terminalId, "starttime asc", "", sdate, edate);
		travelInfoList.clear();
		for(TravelInfo tif:tmpList)
			travelInfoList.add(tif);
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
				ft.addToBackStack(null);
				ft.commit();
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
						dataRefresh();
					}
				}, 0, 0, true);
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
						dataRefresh();
					}
				}, hourOfDay, minute, true);
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
				start_btn.setText(starttime);
				end_btn.setText(endtime);
				dataRefresh();
			}
		});

		left_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tmonth-=1;
				if(tmonth<0){
					tmonth=11;
					tyear-=1;
				}
				date_btn.setText(getDate(tyear,tmonth,tday));
				dataRefresh();
			}
		});

		right_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tmonth+=1;
				if(tmonth>11){
					tmonth=0;
					tyear+=1;
				}
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

			travelinfo_startt_textview.setText(starttime);
			travelinfo_endt_textview.setText(endtime);
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
