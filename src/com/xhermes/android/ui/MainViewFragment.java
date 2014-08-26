package com.xhermes.android.ui;

import java.text.DecimalFormat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhermes.android.R;
import com.xhermes.android.util.MyThreadFor;
import com.xhermes.android.util.OverallFragmentController;

public class MainViewFragment extends Fragment{

	//	private TextView exm_mark_title;
	private TextView exm_mark;
	private TextView exm_comment;
	private Button button_functional_1;
	private Button button_functional_4,button_functional_2,button_functional_3;
	private TextView distance_textview;
	private TextView oil_textview;
	private TextView time_textview;
	private TextView distance_introview;
	private TextView oil_introview;
	private TextView speed_introview;
	private TextView car_number_textview;
	private ImageView pointer;
	private ImageView content;
	private int testsize;
	String terminalId;
	String vehicleexm_score;
	String vehicleexm_comment;
	String car_number;
	String today_distance;
	String today_avg_oil;
	String today_total_time;
	String city;
	String cityNum;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		terminalId = getArguments().getString("terminalId");
		car_number = getArguments().getString("car_number");
		vehicleexm_score = getArguments().getString("vehicleexm_score");
		vehicleexm_comment = getArguments().getString("vehicleexm_comment");
		today_distance = getArguments().getString("today_distance");
		today_avg_oil = getArguments().getString("today_avg_oil");
		DecimalFormat   df   =   new   DecimalFormat("#####0.00");   
		today_avg_oil = df.format(Double.parseDouble(today_avg_oil));
		today_total_time = getArguments().getString("today_total_time");
		city = getArguments().getString("city");
		cityNum = getArguments().getString("cityNum");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootview = inflater.inflate(R.layout.main_view, container, false);
		exm_mark = (TextView)rootview.findViewById(R.id.exm_mark);
		//		exm_mark_title = (TextView)rootview.findViewById(R.id.exm_mark_title);
		exm_comment = (TextView)rootview.findViewById(R.id.exm_comment);
		distance_textview = (TextView)rootview.findViewById(R.id.distance_text);
		oil_textview = (TextView)rootview.findViewById(R.id.oil_text);
		time_textview = (TextView)rootview.findViewById(R.id.time_text);
		car_number_textview = (TextView)rootview.findViewById(R.id.car_number);
		button_functional_1 = (Button)rootview.findViewById(R.id.button_functional_1);
		button_functional_4 = (Button)rootview.findViewById(R.id.button_functional_4);
		button_functional_2 = (Button)rootview.findViewById(R.id.button_functional_2);
		button_functional_3 = (Button)rootview.findViewById(R.id.button_functional_3);
		pointer = (ImageView)rootview.findViewById(R.id.pointer);
		//content = (ImageView)rootview.findViewById(R.id.content);
		
		WindowManager w = this.getActivity().getWindowManager();
		int windowHeight = w.getDefaultDisplay().getHeight()/2;
		int windowWidth = w.getDefaultDisplay().getWidth();
		System.out.println(windowHeight + "---" + windowWidth);
		int xx_size = windowHeight<windowWidth?(int)5*windowHeight/12:(int)5*windowWidth/12;
		int yy_size = (int)xx_size*350/375;
		int middle_size = windowHeight<windowWidth?(int)362*windowHeight/900:(int)362*windowWidth/900;
		System.out.println(xx_size+ "---" + yy_size);
		button_functional_1.getLayoutParams().width = yy_size;
		button_functional_1.getLayoutParams().height = xx_size;
		button_functional_2.getLayoutParams().width = xx_size;
		button_functional_2.getLayoutParams().height = yy_size;
		button_functional_3.getLayoutParams().width = yy_size;
		button_functional_3.getLayoutParams().height = xx_size;
		button_functional_4.getLayoutParams().width = xx_size;
		button_functional_4.getLayoutParams().height = yy_size;
		pointer.getLayoutParams().width = middle_size;
		pointer.getLayoutParams().height = middle_size;
		//content.getLayoutParams().width = middle_size;
		//content.getLayoutParams().height = middle_size;
//		ViewTreeObserver vto1 = button_functional_1.getViewTreeObserver();
//		vto1.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
//        {
//			boolean measured_btn1 = false;
//            public boolean onPreDraw()
//            {
//            	if(!measured_btn1){
//                    int height = button_functional_1.getMeasuredHeight();
//                    int width = button_functional_1.getMeasuredWidth();
//                    System.out.println("001:" + height);
//                    System.out.println("001:" + width);
//                    measured_btn1 = true;
//            	}
//                return true;
//            }
//        });


		distance_introview=(TextView)rootview.findViewById(R.id.distance_intro);
		oil_introview=(TextView)rootview.findViewById(R.id.oil_intro);
		speed_introview=(TextView)rootview.findViewById(R.id.speed_intro);


		//		exm_mark_title.setText(R.string.exm_mark_title);
		//		exm_mark_title.setTextSize(18);
		//		exm_mark_title.setTextColor(Color.rgb(210, 228, 228));

		exm_mark.setTextColor(Color.WHITE);
		String mark_text = "0分";
		Spannable word = new SpannableString(mark_text);
		int start = 0;
		int end = mark_text.indexOf("分");
		DisplayMetrics dm = exm_mark.getResources().getDisplayMetrics();
		int defaultSize=40;
		testsize = (int) (defaultSize* dm.density);
		word.setSpan(new AbsoluteSizeSpan(100), start, end,   

				Spannable.SPAN_INCLUSIVE_INCLUSIVE);  

		word.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end,   

				Spannable.SPAN_INCLUSIVE_INCLUSIVE);  

		exm_mark.setText(word);
		//exm_mark.setTextSize(testsize);
		exm_comment.setTextColor(Color.rgb(210, 228, 228));
		exm_comment.setText(vehicleexm_comment);

		car_number_textview.setText(car_number);
		DisplayMetrics dm2 = distance_textview.getResources().getDisplayMetrics();
		int defaultSize2=6;
		int testsize2 = (int) (defaultSize2* dm2.density);
		int defaultSize3=30;
		int testsize3 = (int) (defaultSize3* dm2.density);


		String distance_text = today_distance + "公里";
		Spannable distance_word = new SpannableString(distance_text);
		int distance_start = 0;
		int distance_end = distance_text.indexOf("公里");
		distance_word.setSpan(new AbsoluteSizeSpan(testsize3), distance_start, distance_end,   

				Spannable.SPAN_INCLUSIVE_INCLUSIVE);  

		distance_word.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), distance_start, distance_end,   

				Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
		distance_textview.setText(distance_word);	
		distance_textview.setTextSize(testsize2);
		distance_introview.setTextSize(testsize2);
		oil_introview.setTextSize(testsize2);
		speed_introview.setTextSize(testsize2);
		String oil_text = today_avg_oil + "百公里升";
		Spannable oil_word = new SpannableString(oil_text);
		int oil_start = 0;
		int oil_end = oil_text.indexOf("百公里升");
		oil_word.setSpan(new AbsoluteSizeSpan(testsize3), oil_start, oil_end,   

				Spannable.SPAN_INCLUSIVE_INCLUSIVE);  

		oil_word.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), oil_start, oil_end,   

				Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
		oil_textview.setText(oil_word);

		oil_textview.setTextSize(testsize2);

		String time_text = today_total_time + "分钟";
		Spannable time_word = new SpannableString(time_text);
		int time_start = 0;
		int time_end = time_text.indexOf("分钟");
		time_word.setSpan(new AbsoluteSizeSpan(testsize3), time_start, time_end,   

				Spannable.SPAN_INCLUSIVE_INCLUSIVE);  

		time_word.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), time_start, time_end,   

				Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
		time_textview.setText(time_word);
		time_textview.setTextSize(testsize2);

		MyHandler handler = new MyHandler();
		new Thread(new MyThreadFor(vehicleexm_score, handler)).start();

		//		int width=dm2.widthPixels;
		//		int w=(int) (width*0.8/2);
		//		RelativeLayout.LayoutParams layoutParams1 =new RelativeLayout.LayoutParams(w*13/12,w);
		//		RelativeLayout.LayoutParams layoutParams2 =new RelativeLayout.LayoutParams(w,w*13/12);
		//		RelativeLayout.LayoutParams layoutParams3 =new RelativeLayout.LayoutParams(w*13/12,w);
		//		RelativeLayout.LayoutParams layoutParams4 =new RelativeLayout.LayoutParams(w,w*13/12);
		//		
		//		layoutParams1.setMargins(w, 0, 0, 0);
		//		button_functional_1.setLayoutParams(layoutParams1);
		//		layoutParams2.setMargins(w, w, 0, 0);
		//		button_functional_2.setLayoutParams(layoutParams2);
		//		layoutParams3.setMargins(0, w*11/12, 0, 0);
		//		button_functional_3.setLayoutParams(layoutParams3);
		//		layoutParams4.setMargins(0, 0, 0, 0);
		//		button_functional_4.setLayoutParams(layoutParams4);

		
		pointer.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Bundle bundle = new Bundle();  
//				bundle.putString("terminalId", terminalId);
//				VehicleExmFragment vFragment = new VehicleExmFragment(); 
//				vFragment.setArguments(bundle);
//				OverallFragmentController.removeFragment("exam");
//				OverallFragmentController.addFragment("exam", vFragment);
//				FragmentManager fm=getFragmentManager();
//				FragmentTransaction transaction = fm.beginTransaction();
//				transaction.replace(R.id.fragment_container, vFragment); 
//				transaction.commit();
			}
		});
		button_functional_1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();  
				bundle.putString("terminalId", terminalId);
				VehicleExmFragment vFragment = new VehicleExmFragment(); 
				vFragment.setArguments(bundle);
				OverallFragmentController.removeFragment("exam");
				OverallFragmentController.addFragment("exam", vFragment);
				FragmentManager fm=getFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				transaction.replace(R.id.fragment_container, vFragment); 
				transaction.commit();
			}
		});
		button_functional_2.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();  
				bundle.putString("terminalId", terminalId);
				DrivingHabitFragment dFragment = new DrivingHabitFragment(); 
				dFragment.setArguments(bundle);
				OverallFragmentController.removeFragment("habit");
				OverallFragmentController.addFragment("habit", dFragment);
				FragmentManager fm=getFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				transaction.replace(R.id.fragment_container, dFragment); 
				transaction.commit();
			}
		});
		button_functional_3.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();  
				bundle.putString("terminalId", terminalId);
				VehicleInfoFragment viFragment = new VehicleInfoFragment(); 
				viFragment.setArguments(bundle);
				OverallFragmentController.removeFragment("info");
				OverallFragmentController.addFragment("info", viFragment);
				FragmentManager fm=getFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				transaction.replace(R.id.fragment_container, viFragment); 
				transaction.commit();
			}
		});
		button_functional_4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();  
				bundle.putString("terminalId", terminalId);
				MapFragment mFragment = new MapFragment(); 
				mFragment.setArguments(bundle);
				OverallFragmentController.removeFragment("map");
				OverallFragmentController.addFragment("map", mFragment);
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.fragment_container, mFragment); 
				transaction.addToBackStack(null);
				transaction.commit();
			}

		});
		OverallFragmentController.mainFragment_over_created = true;
		return rootview;
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
			if (msg.what == 2) {  
				Bundle b = msg.getData();  
				String textviewContent = b.getString("textviewContent"); 
				int start = 0;
				int end = textviewContent.indexOf("分");
				textviewContent = textviewContent.split("分")[0];
				Spannable word = new SpannableString(textviewContent);
				int colorInt = Integer.parseInt(textviewContent.substring(start,end));
				word.setSpan(new AbsoluteSizeSpan(testsize), start, end,   
						Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
				word.setSpan(new StyleSpan(Typeface.BOLD), start, end,   
						Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
				word.setSpan(new ForegroundColorSpan(Color.rgb((int)(255-colorInt*2.55), (int)(colorInt*2.55), 0)), start, end,   
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
				exm_mark.setText(word);
			}
			//System.out.println("handler线程ID:"+Thread.currentThread().getId());  
		}  
	}
}
