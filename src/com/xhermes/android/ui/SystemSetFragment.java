package com.xhermes.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.xhermes.android.R;
import com.xhermes.android.util.SystemSetControl;

public class SystemSetFragment extends Fragment{
	Switch isBeepSw;
	Switch isNoticeReceiveSw;
	Switch isShockSw;
	Activity act;
	SystemSetControl syscontrol;
	Button pwdChange_btn,logout_btn,exit_btn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act=getActivity();
		syscontrol=new SystemSetControl(act);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root=inflater.inflate(R.layout.systemset_view, null);
		isBeepSw=(Switch) root.findViewById(R.id.isBeep_switchBtn);
		isNoticeReceiveSw=(Switch) root.findViewById(R.id.isNoticeReceive_switchBtn);
		isShockSw=(Switch) root.findViewById(R.id.isShock_switchBtn);
		isBeepSw.setChecked(syscontrol.isBeep());
		isShockSw.setChecked(syscontrol.isShock());
		isNoticeReceiveSw.setChecked(syscontrol.isNoticeReceive());
		
		pwdChange_btn=(Button) root.findViewById(R.id.pwdChange_btn);
		logout_btn=(Button) root.findViewById(R.id.logout_btn);
		exit_btn=(Button) root.findViewById(R.id.exit_btn);

		init();
		return root;
	}

	public void init(){
		isBeepSw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				syscontrol.setBeep(arg1);
			}
		});
		isShockSw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				syscontrol.setShock(arg1);
			}
		});
		isNoticeReceiveSw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				syscontrol.setNoticeReceive(arg1);
			}
		});
		
		OnClickListener onclickListener=new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(view.equals(logout_btn)){
					syscontrol.setAutoLog(false);
					Intent intent=new Intent(act,LoginActivity.class);
					startActivity(intent);
				}else if(view.equals(exit_btn)){
					//act.finish();
					System.exit(0);
				}else if(view.equals(pwdChange_btn)){
					
				}
			}
		};
		
		pwdChange_btn.setOnClickListener(onclickListener);
		logout_btn.setOnClickListener(onclickListener);
		exit_btn.setOnClickListener(onclickListener);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
