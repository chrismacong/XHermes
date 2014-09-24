package com.xhermes.android.network;

public class URLMaker {
	private final static String URL = "http://112.124.47.134/OBDController/";
	public static String makeURL(String part_url){
		return URL + part_url;
	}
}
