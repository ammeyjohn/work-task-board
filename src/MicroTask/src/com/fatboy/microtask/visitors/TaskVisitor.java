package com.fatboy.microtask.visitors;

import java.lang.reflect.Type;
import java.util.List;

import com.fatboy.microtask.models.ApiResponse;
import com.fatboy.microtask.models.Task;
import com.fatboy.microtask.utils.Network;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TaskVisitor {
	
	public final static String ACTION_TASK_LIST = "/tasks/list";
	public final static String ACTION_TASK_ADD  = "/task/add";
	
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
	
	public List<Task> getTasks(int projectId) {
		
		String url = Network.BASE_URL + ACTION_TASK_LIST;
		if(projectId >= 0) {
			url += "?pid=" + projectId;
		}
		String html = Network.Requst(url);

        Gson gson = new Gson();
        Type type = new TypeToken<ApiResponse<List<Task>>>(){}.getType();
        ApiResponse<List<Task>> api = gson.fromJson(html, type);
        
        if(api.getResult()) {
        	return api.getData();
        }
        
        return null;
	}
	
/*	public Boolean createTask(Task task) {
		
		String url = Network.BASE_URL + ACTION_TASK_ADD;
		if (task != null) {
			url += 
		}
	}*/
}
