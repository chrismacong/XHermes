package com.xhermes.android.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.xhermes.android.R;
import com.xhermes.android.network.URLMaker;
import com.xhermes.android.util.CustomDialog;
import com.xhermes.android.util.SystemSetControl;
import com.xhermes.android.util.Utilities;

public class LoginActivity extends Activity {

	private TextView forget_pwd_textview;
	private final String TEST_USER_NAME = "user@gmail.com";
	private EditText username_edittext;
	private EditText password_edittext;
	private Button sign_in_as_user;
	private Button sign_in_as_customer;
	private ProgressDialog pd;
	private final String check_user_url =  URLMaker.makeURL("login/mobilelogin.html");
	private final String forget_pwd_url = URLMaker.makeURL("mail/forget_password_sendmail.html");

	private SharedPreferences preference;
	private CheckBox saveUser,autoLog;
	
	private SystemSetControl syscontrol;
	
	private void saveUser(String username,String pwd){
		Editor editor=preference.edit();
		if(saveUser.isChecked()){
			syscontrol.setSaved(true);
			syscontrol.saveUser(username, pwd);
		}else
			syscontrol.setSaved(false);
		
		if(autoLog.isChecked())
			syscontrol.setAutoLog(true);
		else
			syscontrol.setAutoLog(false);
	}

	private void loadUser(){
		if(syscontrol.isSaved()){
			saveUser.setChecked(true);
			username_edittext.setText(syscontrol.getUsername());
			password_edittext.setText(syscontrol.getPwd());
		}else
			saveUser.setChecked(false);
		
		if(syscontrol.isAutoLog()){
			autoLog.setChecked(true);
			sign_in_as_user.performClick();
		}
		else
			autoLog.setChecked(false);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		syscontrol=new SystemSetControl(this);
		
		username_edittext = (EditText)findViewById(R.id.username);
		password_edittext = (EditText)findViewById(R.id.password);
		forget_pwd_textview = (TextView)findViewById(R.id.forget_psw);
		forget_pwd_textview.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		sign_in_as_user = (Button)findViewById(R.id.sign_in_button);
		sign_in_as_customer = (Button)findViewById(R.id.customer_button);

		saveUser=(CheckBox) findViewById(R.id.saveUserCheckBox);
		autoLog=(CheckBox) findViewById(R.id.autoLoginCheckBox);
		preference=getSharedPreferences("userinfo",MODE_PRIVATE);
		
//		username_edittext.setText(TEST_USER_NAME);
//		password_edittext.setText("123456");
		password_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {  
			@Override  
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
				/*判断是否是“GO”键*/  
				if(actionId == EditorInfo.IME_ACTION_GO){  
					/*隐藏软键盘*/  
					InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
					if (imm.isActive()) {  
						imm.hideSoftInputFromWindow(  
								v.getApplicationWindowToken(), 0);  
					}  
					sign_in_as_user.performClick();
					return true;  
				}  
				return false;  
			}  
		});
		sign_in_as_user.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				final String username_uncheck = username_edittext.getText().toString();
				final String password_uncheck = password_edittext.getText().toString();
				Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");  
				Matcher m = p.matcher(username_uncheck);  
				boolean b = m.matches();  
				Pattern _p = Pattern.compile("[A-Za-z0-9]+");
				Matcher _m = _p.matcher(password_uncheck);  
				boolean _b = _m.matches();  
				if(!b){
					Utilities.showMessage(LoginActivity.this, R.string.username_unpass);
				}
				else{
					if(!_b){
						Utilities.showMessage(LoginActivity.this, R.string.password_unpass);
					}
					else{
						pd=new CustomProgressDialog(LoginActivity.this, R.style.dialog, "正在登陆…");
						pd.setCanceledOnTouchOutside(false);
						pd.show();
						new AsyncTask<Void, Void, String>() {
							String username = username_uncheck;
							String password = password_uncheck;
							@Override
							protected void onPreExecute() {

							}

							@Override
							protected String doInBackground(Void... voids) {
								String result = "";
								
								//保存
								saveUser(username,password);
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("email", username));
								params.add(new BasicNameValuePair("pwd", password));
								try {
									result = com.xhermes.android.network.HttpClient.getInstance().httpPost(check_user_url, params);
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
								final String[] signs = signInResult.split(";");
								if(signs.length<=1){
									pd.dismiss();
									Utilities.showMessage(LoginActivity.this, R.string.network_failed);
								}
								else{
									System.out.println(signs[0]);
									final int passOrNot = Integer.parseInt(signs[0]);
									final String terminalId = signs[1];
									if(passOrNot==0){
										pd.dismiss();
										Utilities.showMessage(LoginActivity.this, R.string.check_failed);
									}
									else{
										JPushInterface.setDebugMode(true);
										System.out.println("0001");
										JPushInterface.init(LoginActivity.this);

										System.out.println("0002");
										JPushInterface.setAlias(LoginActivity.this, terminalId, new TagAliasCallback() {
											@Override
											public void gotResult(int code, String alias, Set<String> strings) {
												Intent intent = new Intent(LoginActivity.this, MainActivity.class);

												System.out.println("0003");
												System.out
														.println(signs.length);
												Bundle bundle = new Bundle();//　　Bundle的底层是一个HashMap<String, Object
												bundle.putString("terminalId", terminalId);
												bundle.putString("email", username);
												bundle.putString("car_number",signs[2]);
												bundle.putString("vehicleexm_score",signs[3]);
												bundle.putString("vehicleexm_comment", signs[4]);
												bundle.putString("today_distance",signs[5]);
												bundle.putString("today_total_oil", signs[6]);
												bundle.putString("today_avg_oil", signs[7]);
												bundle.putString("today_avg_speed", signs[8]);
												bundle.putString("today_total_time", signs[9]);
												bundle.putString("today_brake_times", signs[10]);
												bundle.putString("today_emer_brake_times", signs[11]);
												bundle.putString("today_speedup_times", signs[12]);
												bundle.putString("today_emer_speedup_times", signs[13]);
												bundle.putString("today_max_speed", signs[14]);
												bundle.putString("today_travel_times", signs[15]);
												if(signs.length>16){
													bundle.putString("city", signs[16]);
													bundle.putString("cityNum", signs[17]);
												}
												intent.putExtra("bundle", bundle);

												System.out.println("0004");
												pd.dismiss();

												System.out.println("0005");
												startActivity(intent);
												finish();
											}
										});
									}
								}
							}
						}.execute();
					}
				}
			}

		});

		forget_pwd_textview.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				EditText edittext = new EditText(LoginActivity.this); 
				edittext.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				final EditText et = edittext;
				new AlertDialog.Builder(LoginActivity.this).setTitle("请输入您的邮箱账号").setIcon(
						android.R.drawable.ic_dialog_info).setView(
								et).setPositiveButton("确定", new DialogInterface.OnClickListener(){

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										final String forget_email = et.getText().toString();
										Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");  
										Matcher m = p.matcher(forget_email);  
										boolean b = m.matches();  
										if(!b){
											CustomDialog.Builder builder = new CustomDialog.Builder(LoginActivity.this);  
											builder.setMessage(R.string.username_unpass);  
											builder.setTitle("找回密码"); 
											builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
												public void onClick(DialogInterface dialog, int which) {  
													dialog.dismiss();  
												}  
											});  
											builder.create().show();  
										}
										else{
											pd= ProgressDialog.show(LoginActivity.this, "XHermes", "正在发送邮件…");
											new AsyncTask<Void, Void, String>() {
												@Override
												protected void onPreExecute() {

												}

												@Override
												protected String doInBackground(Void... voids) {
													String result = "";
													List<NameValuePair> params = new ArrayList<NameValuePair>();
													params.add(new BasicNameValuePair("email", forget_email));
													try {
														result = com.xhermes.android.network.HttpClient.getInstance().httpPost(forget_pwd_url,params);
													} catch (IOException e) {
														e.printStackTrace();
													}
													return result;
												}

												@Override
												protected void onPostExecute(String signInResult) {
													pd.dismiss();
													signInResult = signInResult.trim();
													try {
														JSONObject p = new JSONObject(signInResult);
														String response_bool=(String) p.get("success");
														if("true".equals(response_bool)){
															CustomDialog.Builder builder = new CustomDialog.Builder(LoginActivity.this);  
															builder.setMessage("邮件已发送，请登录邮箱找回密码");  
															builder.setTitle("XHermes密码找回");  
															builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
																public void onClick(DialogInterface dialog, int which) {  
																	dialog.dismiss();  
																	//设置你的操作事项  
																}  
															});  
															builder.create().show();  
														}
														else{
															CustomDialog.Builder builder = new CustomDialog.Builder(LoginActivity.this);  
															builder.setMessage("您输入的邮箱账号不存在");  
															builder.setTitle("XHermes密码找回");  
															builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
																public void onClick(DialogInterface dialog, int which) {  
																	dialog.dismiss();  
																	//设置你的操作事项  
																}  
															});  
															builder.create().show();  
														}
													} catch (JSONException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												}
											}.execute();
										}
									}

								})
								.setNegativeButton("取消", null).show();
			}

		});
		//载入用户
		loadUser();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}
}
