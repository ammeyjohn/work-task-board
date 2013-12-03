package com.fatboy.microtask.visitors;

import java.lang.reflect.Type;
import java.util.List;

import com.fatboy.microtask.utils.*;
import com.fatboy.microtask.models.ApiResponse;
import com.fatboy.microtask.models.User;
import com.fatboy.microtask.utils.Network;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserVisitor {
	
	public final static String ACTION_USER_LIST = "/users/list";
	
	public List<User> getUsers() {
		String url = Network.BASE_URL + ACTION_USER_LIST;
		String html = Network.Requst(url);

        Gson gson = Utils.createGson();
        Type type = new TypeToken<ApiResponse<List<User>>>(){}.getType();
        ApiResponse<List<User>> api = gson.fromJson(html, type);
        
        if(api == null) return null;
        if(api.getResult()) {
        	return api.getData();
        }
        return null;
	}

}
