package com.fatboy.microtask.visitors;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;

import com.fatboy.microtask.models.ApiResponse;
import com.fatboy.microtask.models.Project;
import com.fatboy.microtask.utils.Network;
import com.fatboy.microtask.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ProjectVisitor {
	
	public final static String ACTION_PROJECT_LIST = "/projects/list";
	public final static String ACTION_PROJECT_ADD  = "/project/add";
	public final static String ACTION_PROJECT_DEL  = "/project/del";
	
	public List<Project> getProjects() {
		String url = Network.BASE_URL + ACTION_PROJECT_LIST;
		String html = Network.Requst(url);

        Gson gson = Utils.createGson(); 
        Type type = new TypeToken<ApiResponse<List<Project>>>(){}.getType();
        ApiResponse<List<Project>> api = gson.fromJson(html, type);
        
        if(api.getResult()) {
        	return api.getData();
        }
        
        return null;
	}
	
	public int addProject(Project project) {
		String url = Network.BASE_URL + ACTION_PROJECT_ADD + "?";
		url += "n=" + URLEncoder.encode(project.getProjectName());
		url += "&uid=1";
		
		String desc = project.getDescription();
		if(desc != null && !desc.isEmpty()) {
			url += "&desc=" + URLEncoder.encode(desc);
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
	
	public Boolean delProject(int projectId) {
		String url = Network.BASE_URL + ACTION_PROJECT_DEL + "?";
		url += "id=" + projectId;
		
		String html = Network.Requst(url);
		
        Gson gson = Utils.createGson(); 
        Type type = new TypeToken<ApiResponse<Integer>>(){}.getType();
        ApiResponse<Integer> api = gson.fromJson(html, type);
        
        return api.getResult();
	}
}
