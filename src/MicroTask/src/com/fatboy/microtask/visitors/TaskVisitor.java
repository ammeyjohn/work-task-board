package com.fatboy.microtask.visitors;

import java.lang.reflect.Type;
import java.util.List;

import com.fatboy.microtask.models.ApiResponse;
import com.fatboy.microtask.models.Task;
import com.fatboy.microtask.utils.Network;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class TaskVisitor {
	
	public final static String ACTION_TASK_LIST = "/tasks/list";
	public final static String ACTION_TASK_ADD  = "/task/add";
	
	public List<Task> getTasks() {
		
		String url = Network.BASE_URL + ACTION_TASK_LIST;
		String html = Network.Requst(url);

        Gson gson = createSerilizer();
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

        Gson gson = createSerilizer();
        Type type = new TypeToken<ApiResponse<List<Task>>>(){}.getType();
        ApiResponse<List<Task>> api = gson.fromJson(html, type);
        
        if(api.getResult()) {
        	return api.getData();
        }
        
        return null;
	}
	
	public Task getTask(int taskId) {
		
		String url = Network.BASE_URL + ACTION_TASK_LIST;
		url += "?id=" + taskId;
		
		String html = Network.Requst(url);

        Gson gson = createSerilizer();
        Type type = new TypeToken<ApiResponse<List<Task>>>(){}.getType();
        ApiResponse<List<Task>> api = gson.fromJson(html, type);
        
        if(api.getResult()) {
        	List<Task> tasks = api.getData();
        	if(tasks.size() > 0) {
        		return tasks.get(0);
        	}
        }
        
        return null;
	}
	
	private Gson createSerilizer() {
		return new GsonBuilder()
			.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
			.create();
	}
	
/*	public Boolean createTask(Task task) {
		
		String url = Network.BASE_URL + ACTION_TASK_ADD;
		if (task != null) {
			url += 
		}
	}*/
}
