package com.xhermes.android.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.xhermes.android.R;

public class MyExmThread implements Runnable {
	private String content;
	private Handler handler; 
	private ArrayList<HashMap<String, Object>> listItems;
	private final String ASSORT_NAME_1 = "动力总成系统";
	private final String ASSORT_NAME_2 = "底盘系统";
	private final String ASSORT_NAME_3 = "车身系统";
	private final String ASSORT_NAME_4 = "网络通信";
	private final int[] R_IMAGES = {R.drawable.exm_front_1, R.drawable.exm_front_2, R.drawable.exm_front_3, R.drawable.exm_front_4};
	private final int[] R_DOING_IMAGEVIEWS = {R.id.doing_image_1, R.id.doing_image_2, R.id.doing_image_3, R.id.doing_image_4};
	private final String[] ASSORT_NAMES = {ASSORT_NAME_1, ASSORT_NAME_2, ASSORT_NAME_3, ASSORT_NAME_4};
	private final String[] MAIN_SOLUTIONS = {"您的爱车状况极度危险，请立刻到相关单位进行全面检修！",
			"爱车存在严重问题，请立刻进行检修！",
			"爱车存在大量危险故障，请尽快进行检修",
			"爱车处于不安全状态，抽时间去检修吧！",
			"爱车存在可优化的项目，请注意车辆健康！",
			"爱车健康状况非常好，请继续保持！"
	};	
	public MyExmThread(String content, ArrayList<HashMap<String, Object>> listItems, Handler handler){
		this.content = content;
		this.listItems = listItems;
		this.handler = handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//故障分类
		String[] faultcodes = content.split(",");
		FaultCodeXMLUtil f = new FaultCodeXMLUtil();
		ArrayList<FaultCodeIterator> list = f.getAllFaultCodes();
		ArrayList<String> type_name_list = new ArrayList<String>();
		ArrayList<String> assort_name_list = new ArrayList<String>();
		int SCORE = 100;
		int COUNT_EMERGENCY = 0;
		int COUNT_DANGER = 0;
		int COUNT_RISK = 0;
		int COUNT_WARN = 0;
		int COUNT_NOTICE = 0;
		for(int i=0;i<list.size();i++){
			boolean hasType = false;
			for(int j=0;j<type_name_list.size();j++){
				if(list.get(i).getClassify().equals(type_name_list.get(j))){
					hasType = true;
				}
			}
			if(!hasType){
				type_name_list.add(list.get(i).getClassify());
				assort_name_list.add(list.get(i).getAssortment());
			}
		}
		Message sort_msg = new Message();
		sort_msg.what = 1;
		Bundle sort_bundle = new Bundle();
		sort_bundle.putInt("index", listItems.size()-1);
		sort_bundle.putInt("exmlist_result_image", R.drawable.pass);
		sort_bundle.putInt("mp", 15);
		sort_msg.setData(sort_bundle);
		handler.sendMessage(sort_msg);
		
		//获取体检数据--结束
		Message assort_0_msg = new Message();
		assort_0_msg.what = 1;
		Bundle assort_0_bundle = new Bundle();
		assort_0_bundle.putInt("index", 0);
		assort_0_bundle.putInt("exmlist_result_image", R.drawable.pass);
		assort_0_bundle.putInt("mp", 20);
		assort_0_msg.setData(assort_0_bundle);
		handler.sendMessage(assort_0_msg);
		int list_size = listItems.size();
		//开始分支系统检测
		int mp_improve_each_subexm = 80/type_name_list.size();
		int mp_now = 20;
		for(int i=0;i<ASSORT_NAMES.length;i++){
			//分支系统开始检测，设置图标为loading
			Message assort_x_start_msg = new Message();
			assort_x_start_msg.what = 2;
			Bundle assort_x_start_bundle = new Bundle();
			assort_x_start_bundle.putInt("exmlist_front_image", R_IMAGES[i]);
			assort_x_start_bundle.putString("exmlist_text", ASSORT_NAMES[i]);
			assort_x_start_bundle.putInt("exmlist_result_image", R.drawable.loading);
			assort_x_start_msg.setData(assort_x_start_bundle);
			handler.sendMessage(assort_x_start_msg);
			list_size++;
			boolean assort_passOrNot = true;
			int assort_index = list_size-1;
			//分支检查
			for(int j=0;j<type_name_list.size();j++){
				if(assort_name_list.get(j).equals(ASSORT_NAMES[i])){
					Message type_x_start_msg = new Message();
					type_x_start_msg.what = 2;
					Bundle type_x_start_bundle = new Bundle();
					type_x_start_bundle.putInt("exmlist_front_image", R.drawable.exm_front_mini);
					type_x_start_bundle.putString("exmlist_text", type_name_list.get(j));
					type_x_start_bundle.putInt("exmlist_result_image", R.drawable.loading);
					type_x_start_msg.setData(type_x_start_bundle);
					handler.sendMessage(type_x_start_msg);
					list_size++;
					//体检太快了，加个随机休眠时间，休息休息~
					int index = (int) (Math.random()*500);
					try {
						Thread.sleep(index);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//处理当前分支体检项目，若出现问题，提示错误信息。
					boolean passOrNot = true;
					int title_index = list_size-1;
					for(int k=0;k<list.size();k++){
						if(list.get(k).getClassify().equals(type_name_list.get(j))){
							for(int m=0;m<faultcodes.length;m++){
								if(list.get(k).getIndex().equals(faultcodes[m])){
									passOrNot = false;
									assort_passOrNot = false;
									int priorty_image = R.drawable.exm_front_error1;
									if(list.get(k).getPriorty().equals("NIL")){
										COUNT_NOTICE++;
										if(SCORE>0)
											SCORE -= 1;
									}
									else{
										switch(Integer.parseInt(list.get(k).getPriorty())){
										case 1:
											COUNT_NOTICE++;
											priorty_image = R.drawable.exm_front_error1;
											if(SCORE>0)
												SCORE -= 1;
											break;
										case 2:
											COUNT_WARN++;
											priorty_image = R.drawable.exm_front_error2;
											if(SCORE>0)
												SCORE -= 5;
											break;
										case 3:
											COUNT_RISK++;
											priorty_image = R.drawable.exm_front_error3;
											if(SCORE>0)
												SCORE -= 10;
											break;
										case 4:
											COUNT_DANGER++;
											priorty_image = R.drawable.exm_front_error4;
											if(SCORE>0)
												SCORE -= 20;
											break;
										case 5:
											COUNT_EMERGENCY++;
											priorty_image = R.drawable.exm_front_error5;
											SCORE = 0;
											break;
										default:
											COUNT_NOTICE++;
											priorty_image = R.drawable.exm_front_error1;
											if(SCORE>0)
												SCORE -= 1;
										}
									}
									//加入错误
									Message error_msg = new Message();
									error_msg.what = 5;
									Bundle error_bundle = new Bundle();
									error_bundle.putInt("exmlist_front_image", priorty_image);
									error_bundle.putString("exmlist_text", list.get(k).getFaultDetail() + "\n" + list.get(k).getSolution());
									error_bundle.putInt("exmlist_result_image", R.drawable.empty);
									error_bundle.putInt("score", SCORE);
									error_msg.setData(error_bundle);
									handler.sendMessage(error_msg);
									list_size++;
									//更新mp
								}
							}
						}
					}
					Message type_msg = new Message();
					type_msg.what = 1;
					Bundle type_bundle = new Bundle();
					type_bundle.putInt("index", title_index);
					if(passOrNot)
						type_bundle.putInt("exmlist_result_image", R.drawable.pass);
					else
						type_bundle.putInt("exmlist_result_image", R.drawable.fault);
					mp_now += mp_improve_each_subexm;
					type_bundle.putInt("mp", mp_now);
					type_msg.setData(type_bundle);
					handler.sendMessage(type_msg);
				}
				Message assort_msg = new Message();
				assort_msg.what = 1;
				Bundle assort_bundle = new Bundle();
				assort_bundle.putInt("index", assort_index);
				if(assort_passOrNot)
					assort_bundle.putInt("exmlist_result_image", R.drawable.pass);
				else
					assort_bundle.putInt("exmlist_result_image", R.drawable.fault);
				assort_bundle.putInt("mp", mp_now);
				assort_msg.setData(assort_bundle);
				handler.sendMessage(assort_msg);
				
				//改变顶部完成度图标
				Message doing_msg = new Message();
				doing_msg.what = 4;
				Bundle doing_bundle = new Bundle();
				doing_bundle.putInt("doing_index", R_DOING_IMAGEVIEWS[i]);
				doing_msg.setData(doing_bundle);
				handler.sendMessage(doing_msg);
			}
			Message final_msg = new Message();
			final_msg.what = 3;
			Bundle final_bundle = new Bundle();
			String main_solution = MAIN_SOLUTIONS[(int) Math.floor(SCORE/20)];
			final_bundle.putInt("score", SCORE);
			final_bundle.putString("main_solution", main_solution);
			final_bundle.putInt("mp", 100);
			final_msg.setData(final_bundle);
			handler.sendMessage(final_msg);
		}
	}
}
