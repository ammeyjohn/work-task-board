package com.fatboy.microtask.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {

	public static Gson createGson() {
		return new GsonBuilder()
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
			.create();
	}
	
	private static SimpleDateFormat _Formatter = null;
	
	@SuppressLint("SimpleDateFormat")
	public static String getDateString(Date time) {
		if(_Formatter == null) {
			_Formatter = new SimpleDateFormat("yyyy-MM-dd");
		}
		return _Formatter.format(time);
	}
	
	public static String getNeglectString(int maxLength, String str) {
		if(str == null || str.isEmpty())
			return "";
		if(str.length() >= maxLength) {
			str = str.substring(0, maxLength-3);
			str += "...";
		}
		return str;		
	}
}
