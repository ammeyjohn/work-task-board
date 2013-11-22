package com.fatboy.microtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fatboy.microtask.models.Project;
import com.fatboy.microtask.visitors.ProjectVisitor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
		loadProjects();
		
		// Attach events to ListView
		ListView lsvProjects = (ListView)findViewById(R.id.listProjects);
		lsvProjects.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("项目操作");
				menu.add(0, 1, Menu.NONE, "删除");
				menu.add(0, 2, Menu.NONE, "编辑");					
			}		
		});
		
		
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
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		ListView lsvProjects = (ListView)findViewById(R.id.listProjects);
		final Map<String, Object> map = (Map<String, Object>)lsvProjects.getAdapter().getItem(info.position);
		switch(item.getItemId()) {
		case Menu.FIRST:
			String name = map.get("txtName").toString();
			// Show dialog to confirm deleting project.
			AlertDialog.Builder builder = new Builder(Outer);
			builder.setMessage("确认删除项目" + name + "吗？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteProject(Integer.parseInt(map.get("id").toString()));
				}					
			});
			builder.setNegativeButton("取消", null);
			builder.create().show();
			
			break;
		case Menu.FIRST + 1:								
			break;
		}

		return false; 

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
                Intent intent = new Intent();     
                intent.setClass(ProjectListActivity.this, NewProjectActivity.class);
                startActivity(intent);  
                this.finish();
                break;
            case R.id.menu_refresh_projectlist:
            	loadProjects();
                break;
        }
        return false;
    }
	
	private void loadProjects() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				initinalActivity();
			}
		}).start();
	}
	
	private void deleteProject(final int projectId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ProjectVisitor v = new ProjectVisitor();
				Boolean r = v.delProject(projectId);
				
				if (r) {
					loadProjects();
				}
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
            	if(desc == null || desc.isEmpty()) {
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
