package com.xhermes.android.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.xhermes.android.R;
import com.xhermes.android.model.PositionData;
import com.xhermes.android.model.TravelInfo;
import com.xhermes.android.network.HttpClient;
import com.xhermes.android.network.URLMaker;

public class TravelInfoDetailFragment extends Fragment{
	private final String travelinfo_position_url = URLMaker.makeURL("positioninfo/mobilegetposition.html");
	private TextView startt_textview,startp_textview,endt_textview,endp_textview;
	private ImageView map_image_view,startp_image,endp_image;
	private TravelInfo mTravelInfo;
	private Context ctx;
	private ListView travelinfo_detail_view;
	private String terminalId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle b=getArguments();
		terminalId=b.getString("terminalId");
		mTravelInfo=b.getParcelable("travelinfo");
		System.out.println(mTravelInfo.toString());
		ctx=getActivity();
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root=inflater.inflate(R.layout.travelinfo_detail_view, null);
		startt_textview=(TextView) root.findViewById(R.id.detail_startt_TextView);
		startp_textview=(TextView) root.findViewById(R.id.detail_startp_TextView);
		endt_textview=(TextView) root.findViewById(R.id.detail_endt_TextView);
		endp_textview=(TextView) root.findViewById(R.id.detail_endp_TextView);
		map_image_view=(ImageView) root.findViewById(R.id.detail_map_image);
		startp_image=(ImageView) root.findViewById(R.id.detail_start_image_View);
		travelinfo_detail_view=(ListView) root.findViewById(R.id.detail_travelinfo_listview);
		
		startt_textview.setText(mTravelInfo.getStarttime());
		startp_textview.setText(mTravelInfo.getSposition());
		endt_textview.setText(mTravelInfo.getEndtime());
		endp_textview.setText(mTravelInfo.getEposition());
		
		map_image_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.out.println("imageView in");
				
				new AsyncTask<Void, Void, String>() {
					@Override
					protected void onPreExecute() {
					}

					@Override
					protected String doInBackground(Void... arg0) {
						String result="";
						HttpClient httpc=HttpClient.getInstance();
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("terminalId", terminalId));
//						params.add(new BasicNameValuePair("start_time", "140718000000"));
//						params.add(new BasicNameValuePair("stop_time", "140721000000"));
						params.add(new BasicNameValuePair("start_time", mTravelInfo.getStarttime()));
						params.add(new BasicNameValuePair("stop_time",mTravelInfo.getEndtime()));
						
						try {
							result=httpc.httpPost(travelinfo_position_url,params);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return result;
					}
					
					@Override
					protected void onPostExecute(String result) {
						System.out.println(result);
						
						TravelInfoMapFragment timFragment=new TravelInfoMapFragment();
						Bundle b=new Bundle();
						b.putString("terminalId", terminalId);
						b.putString("positionList",result);
						timFragment.setArguments(b);
						FragmentManager fm=((FragmentActivity) ctx).getSupportFragmentManager();
						FragmentTransaction ft =fm.beginTransaction();
						ft.replace(R.id.fragment_container, timFragment, "travel_info_map_fragment");
						ft.addToBackStack(null);
						ft.commit();
					}
				}.execute();		
				
			}
		});
		
		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<String> contents = new ArrayList<String>();
		//initialize titles and contents
		titles.add(getString(R.string.average_speed));
		titles.add(getString(R.string.max_speed));
		titles.add(getString(R.string.total_oil_consuming));
		titles.add(getString(R.string.average_oil));
		titles.add(getString(R.string.fatigue_driving_time));
		titles.add(getString(R.string.timeout_length));
		titles.add(getString(R.string.brake_times));
		titles.add(getString(R.string.urgent_brake_time));
		titles.add(getString(R.string.accelerate_times));
		titles.add(getString(R.string.urgent_accelerate_times));
		titles.add(getString(R.string.engine_highest_rotation_speed));
		titles.add(getString(R.string.engine_highest_temperature));
		titles.add(getString(R.string.voltage));

		contents.add(mTravelInfo.getAverSpeed() + "（km/h）");
		contents.add(mTravelInfo.getMaxSpeed() + "（km/h）");
		contents.add(mTravelInfo.getTotalFuelC() + "（0.01升）");
		contents.add(mTravelInfo.getAverFuelC() + "（0.01百公里升）");
		contents.add(mTravelInfo.getTiredTime() + "（分钟）");
		contents.add(mTravelInfo.getTimeOut() + "（秒）");
		contents.add(mTravelInfo.getBrakes() + "（次）");
		contents.add(mTravelInfo.getEmBrakes() + "（次）");
		contents.add(mTravelInfo.getSpeedUp() + "（次）");
		contents.add(mTravelInfo.getEmSpeedUp() + "（次）");
		contents.add(mTravelInfo.getRpm() + "（转/分钟）");
		contents.add(mTravelInfo.getWaterTemp() + "（摄氏度）");
		contents.add(mTravelInfo.getVoltage() + "（0.1V）");

		MyAdapter mAdapter = new MyAdapter(ctx, R.layout.travelinfo_detail_list_item, titles, contents);
		travelinfo_detail_view.setAdapter(mAdapter);
		return root;
	}

	
	private class MyAdapter extends ArrayAdapter<String> {
		private ArrayList<String> mTitles;
		private ArrayList<String> mContents;
		private LayoutInflater mInflater;
		private int mResourceId;
		public MyAdapter(Context context, int layout, ArrayList<String> titles, ArrayList<String> contents) {
			super(context,layout, contents);
			mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mResourceId = layout;
			mTitles = titles;
			mContents = contents;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(mResourceId, null);
			TextView titleView = (TextView)convertView.findViewById(R.id.detail_travel_info_list_item_title);
			titleView.setText(mTitles.get(position));
			TextView contentView = (TextView) convertView.findViewById(R.id.detail_travel_info_list_item_content);
			String content=mContents.get(position);
			contentView.setText(setSpannable(content));
			return convertView;
		}
		
		public SpannableString setSpannable(String ctn){
			SpannableString sctn=new SpannableString(ctn);
			ForegroundColorSpan fcst = new ForegroundColorSpan(Color.rgb(255, 255, 255)); 
			ForegroundColorSpan fcsq = new ForegroundColorSpan(Color.rgb(200, 200, 200));
			StyleSpan bss = new StyleSpan(android.graphics.Typeface.ITALIC);
			RelativeSizeSpan tsize=new RelativeSizeSpan(0.6f);
			int i=ctn.indexOf("（");
			int j=ctn.indexOf("）")+1;
			sctn.setSpan(fcst, 0, i, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			sctn.setSpan(bss, i, j, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			sctn.setSpan(tsize, i, j, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			return sctn;
		}
	}
}
