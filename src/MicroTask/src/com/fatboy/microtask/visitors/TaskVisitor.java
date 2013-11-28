package com.fatboy.microtask.visitors;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;

import com.fatboy.microtask.models.ApiResponse;
import com.fatboy.microtask.models.Task;
import com.fatboy.microtask.utils.Network;
import com.fatboy.microtask.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TaskVisitor {
	
	public final static String ACTION_TASK_LIST = "/tasks/list";
	public final static String ACTION_TASK_ADD  = "/task/add";
	public final static String ACTION_TASK_MODIFY  = "/task/modify";
	public final static String ACTION_TASK_DEL  = "/task/del";
	
	public List<Task> getTasks() {
		
		String url = Network.BASE_URL + ACTION_TASK_LIST;
		String html = Network.Requst(url);

        Gson gson = Utils.createGson();
        Type type = new TypeToken<ApiResponse<List<Task>>>(){}.getType();
        ApiResponse<List<Task>> api = gson.fromJson(html, type);
        
        if(api.getResult()) {
        	return api.getData();
        }
        return null;
	}
	
	public List<Task> getTasks(int projectId, int userId) {
		
		String url = Network.BASE_URL + ACTION_TASK_LIST;
		url += "?pid=" + projectId;
		url += "&uid=" + userId;
		
		String html = Network.Requst(url);

        Gson gson = Utils.createGson();
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

        Gson gson = Utils.createGson();
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
	
	@SuppressWarnings("deprecation")
	public int createTask(Task task) {
		
		String url = Network.BASE_URL + ACTION_TASK_ADD;
		if (task != null) {
			url += "?c=" + URLEncoder.encode(task.getTaskContent());
			url += "&uid=" + task.getUserId();
			url += "&pid=" + task.getProjectId();
			url += "&exp=" + Utils.getDateString(task.getExpectDate());
			url += "&priority=" + task.getPriority();
			url += "&ass=" + task.getAssignUser();			
		}
		
		String html = Network.Requst(url);

        Gson gson = Utils.createGson();
        Type type = new TypeToken<ApiResponse<Integer>>(){}.getType();
        ApiResponse<Integer> api = gson.fromJson(html, type);
        
        if(api.getResult()) {
        	return api.getData();
        }
        return -1;
	}
	
	@SuppressWarnings("deprecation")
	public Boolean modifyTask(Task task) {
		
		String url = Network.BASE_URL + ACTION_TASK_MODIFY;
		if (task != null) {
			url += "?id=" + task.getTaskId();
			url += "&c=" + URLEncoder.encode(task.getTaskContent());
			url += "&s=" + task.getStatus();
			url += "&uid=" + task.getUserId();
			url += "&pid=" + task.getProjectId();
			url += "&exp=" + Utils.getDateString(task.getExpectDate());
			url += "&priority=" + task.getPriority();
			url += "&ass=" + task.getAssignUser();			
		}
		
		String html = Network.Requst(url);

        Gson gson = Utils.createGson();
        Type type = new TypeToken<ApiResponse<Integer>>(){}.getType();
        ApiResponse<Integer> api = gson.fromJson(html, type);
        
        return api.getResult();
	}
	
	public Boolean deleteTask(int taskId) {
		String url = Network.BASE_URL + ACTION_TASK_DEL;
		url += "?id=" + taskId;
		
		String html = Network.Requst(url);

        Gson gson = Utils.createGson();
        Type type = new TypeToken<ApiResponse<Integer>>(){}.getType();
        ApiResponse<Integer> api = gson.fromJson(html, type);
        
        return api.getResult();
	}
}
