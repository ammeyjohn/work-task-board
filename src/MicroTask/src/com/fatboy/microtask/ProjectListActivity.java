package com.fatboy.microtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ProjectListActivity extends Activity {

	final private Activity Outer = this;
	
	final static int WHAT_INIT_PROJECT_LIST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projectlist);
		
		// Initialize the project list.
		LoadProjects();
		
		// Attach onClick event to ListView
		ListView lsvProjects = (ListView)findViewById(R.id.listProjects);
		lsvProjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
		    @Override  
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  
		        @SuppressWarnings("unchecked")  		       
		        HashMap<String,Object> map = (HashMap<String, Object>) parent.getItemAtPosition(position);  
		        
		        // Start task activity to show relative tasks.		        
                Intent intent = new Intent();     
                intent.setClass(ProjectListActivity.this, TaskListActivity.class);
                intent.putExtra("id", Integer.parseInt(map.get("id").toString()));
                startActivity(intent);                
		        
		        // Debug
		        Toast.makeText(Outer, map.get("txtName").toString(), Toast.LENGTH_SHORT).show();		        
		    }
		});  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_projectlist, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_new_project:
                Toast.makeText(this, "新建", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_refresh_projectlist:
            	LoadProjects();
                break;
        }
        return false;
    }
	
	private void LoadProjects() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				initinalActivity();
			}
		}).start();
	}
	
	private void initinalActivity() {		
		ProjectVisitor v = new ProjectVisitor();
		List<Project> projects = v.getProjects();
		if(projects == null) {
			Toast.makeText(Outer, "Failed to retrieve project list.", Toast.LENGTH_LONG).show();
		}

        Message msg = new Message();
        msg.what = WHAT_INIT_PROJECT_LIST;
        msg.obj = projects;
        handler.sendMessage(msg);
	}
	
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case WHAT_INIT_PROJECT_LIST:
				RefreshProjectList(msg);
				break;
			}
		}
		
        private void RefreshProjectList(Message msg) {
            @SuppressWarnings("unchecked")
			List<Project> projects = (List<Project>)msg.obj;
            List<Map<String, Object>> names = new ArrayList<Map<String, Object>>();
            for(int i = 0; i < projects.size(); i++) {
            	Project prj = projects.get(i);
            	String desc = prj.getDescription();
            	if(desc == null || desc.length() == 0) {
            		desc = prj.getProjectName();
            	}
            	
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", prj.getProjectId());
                map.put("img", R.drawable.ic_project);
                map.put("txtName", prj.getProjectName());
                map.put("txtDesc", desc);
                map.put("txtCreateTime", prj.getCreateTime());
                names.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(
                    Outer,
                    names,
                    R.layout.layout_projectitem,
                    new String[]{"img","txtName","txtDesc","txtCreateTime"},
                    new int[]{R.id.img,R.id.txtName,R.id.txtDesc,R.id.txtCreateTime}
            );

            ListView lsvProjects = (ListView)findViewById(R.id.listProjects);
            lsvProjects.setAdapter(adapter);
            
            Toast.makeText(Outer, "Project list has been refreshed.", Toast.LENGTH_LONG).show();
        }
    };
}
