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
import android.annotation.SuppressLint;
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
	
	final static int WHAT_INIT_PROJECT_LIST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_list);
		
		// Initialize the project list.
		loadProjects();
		
		// Attach events to ListView
		ListView lsvProjects = (ListView)findViewById(R.id.project_list);
		lsvProjects.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.setHeaderTitle(R.string.project_list_context_menu_header_title);
				menu.add(0, 1, Menu.NONE, R.string.project_list_context_menu0_name);
				menu.add(0, 2, Menu.NONE, R.string.project_list_context_menu1_name);
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
		    }
		});
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		ListView lsvProjects = (ListView)findViewById(R.id.project_list);
		@SuppressWarnings("unchecked")
		final Map<String, Object> map = (Map<String, Object>)lsvProjects.getAdapter().getItem(info.position);
		switch(item.getItemId()) {
		case Menu.FIRST:
			// Show dialog to confirm deleting project.
			String name = map.get("item_text_name").toString();
			AlertDialog.Builder builder = new Builder(ProjectListActivity.this);
			builder.setMessage(String.format(getString(R.string.project_list_dialog_message), name));
			builder.setTitle(getString(R.string.project_list_dialog_title));
			builder.setPositiveButton(getString(R.string.project_list_dialog_ok_button), 
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							deleteProject(Integer.parseInt(map.get("id")
									.toString()));
						}
					});
			builder.setNegativeButton(getString(R.string.project_list_dialog_cancel_button), null);
			builder.create().show();
			break;
		case Menu.FIRST + 1:							
            Intent intent = new Intent();     
            intent.setClass(ProjectListActivity.this, ProjectDetailActivity.class);
            intent.putExtra("tag", (Project)map.get("tag"));
            startActivity(intent);  
            this.finish();			
			break;
		}

		return false; 

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_project_list, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_create_project:
                Intent intent = new Intent();     
                intent.setClass(ProjectListActivity.this, ProjectDetailActivity.class);
                startActivity(intent);  
                this.finish();
                break;
            case R.id.menu_refresh_project_list:
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
			String errmsg = getString(R.string.project_list_fail_to_load_projects);
			Toast.makeText(ProjectListActivity.this, errmsg, Toast.LENGTH_LONG).show();
		}

        Message msg = new Message();
        msg.what = WHAT_INIT_PROJECT_LIST;
        msg.obj = projects;
        handler.sendMessage(msg);
	}
	
    private void refreshProjects(List<Project> projects) {
        List<Map<String, Object>> names = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < projects.size(); i++) {
        	Project prj = projects.get(i);
        	String desc = prj.getDescription();
        	if(desc == null || desc.isEmpty()) {
        		desc = prj.getProjectName();
        	}
        	
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", prj.getProjectId());
            map.put("item_image_icon", R.drawable.ic_project);
            map.put("item_text_name", prj.getProjectName());
            map.put("item_text_desc", desc);
            map.put("item_text_time", prj.getCreateTimeString());
            map.put("tag", prj);
            names.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                ProjectListActivity.this,
                names,
                R.layout.layout_project_item,
                new String[]{"item_image_icon","item_text_name","item_text_desc","item_text_time"},
                new int[]{R.id.item_image_icon,R.id.item_text_name,R.id.item_text_desc,R.id.item_text_time}
        );

        ListView lsvProjects = (ListView)findViewById(R.id.project_list);
        lsvProjects.setAdapter(adapter);
    }
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case WHAT_INIT_PROJECT_LIST:
				@SuppressWarnings("unchecked")
				List<Project> projects = (List<Project>)msg.obj;
				refreshProjects(projects);
				break;
			}
		}		
    };
}
