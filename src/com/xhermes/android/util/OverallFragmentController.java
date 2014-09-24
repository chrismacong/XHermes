package com.xhermes.android.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.support.v4.app.Fragment;

public class OverallFragmentController {
	public static Context act;
	public static ArrayList<HashMap> list = new ArrayList<HashMap>();
	public static boolean onMain = true;
	public static boolean mainFragment_over_created = false;
	public static void addFragment(String tag, Fragment f){
		HashMap hashmap = new HashMap();
		hashmap.put("tag", tag);
		hashmap.put("fragment", f);
		list.add(hashmap);
		System.out.println("i:"+list.size());
	}
	
	public static void removeFragment(String tag){
		for(int i=0;i<list.size();i++){
			System.out.println("i:"+i);
			if(list.get(i).get("tag").equals(tag)){
				list.remove(i);
				return;
			}
		}
	}
//	public static void replaceFragment(String tag, Fragment f){
//		removeFragment(tag);
//		addFragment(tag, f);
//	}
//	public static HashMap getFragmentByTag(String tag){
//		for(int i=0;i<list.size();i++){
//			if(list.get(i).get("tag").equals(tag))
//				return list.get(i);
//		}
//		return null;
//	}
	public static HashMap popFragment(){
		if(list.size()>0){
			HashMap map = list.get(list.size()-1);
			if((map.get("tag")).equals("main")){
				onMain = true;
				return map;
			}
			else{
				onMain = false;
				list.remove(list.size()-1);
			}
			HashMap return_map = list.get(list.size()-1);
			return return_map;
		}
		else
			return null;
	}
	public static void removeAll(){
		list.removeAll(list);
	}
}
