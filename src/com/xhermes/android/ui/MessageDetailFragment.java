package com.xhermes.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xhermes.android.R;
import com.xhermes.android.dao.NoticeDao;
import com.xhermes.android.model.Notice;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MessageDetailFragment extends Fragment{
	private int mail_id;
	private String terminalId;
	private Context ctx;
	private TextView title;
	private TextView content;
	private TextView time;
	private TextView publisher;
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub	

		
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View root=inflater.inflate(R.layout.message_detail, null);
		ctx=getActivity();
		Bundle b=getArguments();
		mail_id=b.getInt("id");
		terminalId=b.getString("terminalId");
		NoticeDao ndao=new NoticeDao(ctx);
		Notice notice=ndao.queryById(terminalId, mail_id);
		title=(TextView) root.findViewById(R.id.message_title);
		content=(TextView) root.findViewById(R.id.message_content);
		time=(TextView) root.findViewById(R.id.ptime);
		publisher=(TextView) root.findViewById(R.id.publisher);
		
		title.setText(notice.getTitle());
		content.setText(notice.getContent());
		time.setText(notice.getTime());
		publisher.setText(notice.getSender());
		System.out.print(notice.getSender());
		return root;
	}
}
