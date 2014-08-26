package com.xhermes.android.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xhermes.android.R;
import com.xhermes.android.network.HttpClient;
import com.xhermes.android.network.URLMaker;

public class PwdChangeFragment extends Fragment{
	private final String pwdChange_url=URLMaker.makeURL("/login/mobilechangepwd.html");
	private Activity act;
	private TextView oldpwd_wrong_TextView,newpwd_wrong_TextView,newpwd_wrong_TextView2;
	private EditText oldpwd_edittext,newpwd_edittext,newpwd2_edittext;
	private Button pwdChange_confirm_btn;
	private String oldpwd,newpwd,newpwd2;
	private Bundle bundle;
	private boolean isRight;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act=getActivity();
		bundle=getArguments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root=inflater.inflate(R.layout.pwd_change_view, null);
		oldpwd_wrong_TextView=(TextView) root.findViewById(R.id.oldpwd_wrong_textview);
		newpwd_wrong_TextView=(TextView) root.findViewById(R.id.newpwd_wrong_textview);
		newpwd_wrong_TextView2=(TextView) root.findViewById(R.id.newpwd_check_wrong_textview);
		oldpwd_edittext=(EditText) root.findViewById(R.id.oldpwd_edittext);
		newpwd_edittext=(EditText) root.findViewById(R.id.newpwd_edittext);
		newpwd2_edittext=(EditText) root.findViewById(R.id.newpwd_check_edittext);
		pwdChange_confirm_btn=(Button) root.findViewById(R.id.pwdChange_confirm_btn);

		OnFocusChangeListener fcl=new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean arg1) {
				if(view.equals(oldpwd_edittext)){
					oldpwd=oldpwd_edittext.getText().toString();
					if(!arg1&&(oldpwd==null||oldpwd.equals(""))){
						oldpwd_wrong_TextView.setText("密码不能为空");
					}else{
						oldpwd_wrong_TextView.setText("");
					}
						
				}else if(view.equals(newpwd_edittext)){
					newpwd=newpwd_edittext.getText().toString();
					if(!arg1&&(newpwd==null||newpwd.equals(""))){
						newpwd_wrong_TextView.setText("密码不能为空");
					}else{
						newpwd_wrong_TextView.setText("");
					}
				}else if(view.equals(newpwd2_edittext)){
					newpwd2=newpwd2_edittext.getText().toString();
					if(!arg1){
						if(newpwd2==null||newpwd2.equals("")){
							newpwd_wrong_TextView2.setText("密码不能为空");
						}else if(!newpwd2.equals(newpwd)){
							newpwd_wrong_TextView2.setText("密码不一致");
						}
					}else{
						newpwd_wrong_TextView2.setText("");
					}
				}
			}
		};
		oldpwd_edittext.setOnFocusChangeListener(fcl);
		newpwd_edittext.setOnFocusChangeListener(fcl);
		newpwd2_edittext.setOnFocusChangeListener(fcl);
		
		pwdChange_confirm_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				oldpwd=oldpwd_edittext.getText().toString();
				newpwd=newpwd_edittext.getText().toString();
				newpwd2=newpwd2_edittext.getText().toString();
				isRight=true;
				if(isRight){
					new AsyncTask<Void, Void, String>() {
						@Override
						protected String doInBackground(Void... arg0) {
							String result="";
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("email", bundle.getString("email")));
							params.add(new BasicNameValuePair("pwd", oldpwd));
							params.add(new BasicNameValuePair("newpwd", newpwd));
							System.out.println("send request pwd");
							try {
								result=HttpClient.getInstance().httpPost(pwdChange_url, params);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return result;
						}

						@Override
						protected void onPostExecute(String result) {
							System.out.println("result:"+result+"123");
							result=result.trim();
							if(result.equals("0")){
								System.out.println("i am in ");
								Toast.makeText(act, "密码错误", Toast.LENGTH_SHORT).show();
								oldpwd_wrong_TextView.setText("密码错误");
							}
							else if(result.equals("1")){
								Toast.makeText(act, "与原密码相同", Toast.LENGTH_SHORT).show();
								newpwd_wrong_TextView.setText( "与原密码相同");
							}
							else if(result.equals("2"))
								Toast.makeText(act, "系统繁忙，请稍后尝试", Toast.LENGTH_SHORT).show();
							else if(result.equals("3"))
								Toast.makeText(act, "密码修改成功", Toast.LENGTH_SHORT).show();
								
						}
					}.execute();
				}
			}
		});
		return root;
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
