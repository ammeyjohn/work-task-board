package com.fatboy.microtask;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TaskVisitor {
	
	public final static String ACTION_TASK_LIST = "/tasks/list";
	
	public List<Task> getTasks() {
		
		String url = Network.BASE_URL + ACTION_TASK_LIST;
		String html = Network.Requst(url);

        Gson gson = new Gson();
        Type type = new TypeToken<ApiResponse<List<Task>>>(){}.getType();
        ApiResponse<List<Task>> api = gson.fromJson(html, type);
        
        if(api.getResult()) {
        	return api.getData();
        }
        
        return null;
	}
	
}
