package com.fatboy.microtask.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;

import com.fatboy.microtask.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {

	public static Gson createGson() {
		return new GsonBuilder()
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
			.setDateFormat("yyyy-MM-dd")
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
	
	@SuppressLint("SimpleDateFormat")
	public static String getDateTimeString(Date time) {
		if(_Formatter == null) {
			_Formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
	
	public static User findUserById(List<User> users, int userId) {
		for(User user : users) {
			if(user.getUserId() == userId) {
				return user;
			}
		}
		return null;
	}
	
	public static String[] getStatusText() {
		return new String[]{ "已创建", "已分派", "已完成", "已确定" };
	}
}
