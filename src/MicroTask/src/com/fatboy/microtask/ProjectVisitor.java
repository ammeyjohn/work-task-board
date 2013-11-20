package com.fatboy.microtask;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ProjectVisitor {
	
	public final static String ACTION_PROJECT_LIST = "/projects/list";
	
	public List<Project> getProjects() {
		String url = Network.BASE_URL + ACTION_PROJECT_LIST;
		String html = Network.Requst(url);

        Gson gson = new Gson();
        Type type = new TypeToken<ApiResponse<List<Project>>>(){}.getType();
        ApiResponse<List<Project>> api = gson.fromJson(html, type);
        
        if(api.getResult()) {
        	return api.getData();
        }
        
        return null;
	}
	
}
