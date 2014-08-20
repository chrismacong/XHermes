package com.xhermes.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.xhermes.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MessageDetailFragment extends Fragment{
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View root=inflater.inflate(R.layout.message_detail, null);
		return root;
	}
}
