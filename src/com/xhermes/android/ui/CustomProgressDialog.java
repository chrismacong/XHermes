package com.xhermes.android.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.xhermes.android.R;

public class CustomProgressDialog extends ProgressDialog{
	private String content;
	private TextView  txtView;
	public CustomProgressDialog(Context context, int theme,String content) {
		super(context, theme);
		this.content=content;

	}
	public CustomProgressDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_progress_dialog);
		txtView=(TextView) findViewById(R.id.dialog_textview);
		txtView.setText(content);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}



}
