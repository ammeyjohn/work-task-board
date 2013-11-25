package com.fatboy.microtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fatboy.microtask.models.Task;
import com.fatboy.microtask.visitors.TaskVisitor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
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

public class TaskListActivity extends Activity {

	final static int WHAT_INIT_TASK_LIST = 1;
	private int ProjectId = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		
		// Get relative project id.
		Intent intent = getIntent();
		ProjectId = intent.getIntExtra("id", 0);
		
		// Initialize the task list.
		loadTasks();
		
		// Attach events to ListView
		ListView lsvTasks = (ListView)findViewById(R.id.task_list);
		lsvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
		    @Override  
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  
		        @SuppressWarnings("unchecked")  		       
		        HashMap<String,Object> map = (HashMap<String, Object>) parent.getItemAtPosition(position);  
		        
        		Task task = (Task)map.get("tag");
                Intent intent = new Intent();     
                intent.setClass(TaskListActivity.this, TaskDetailActivity.class);
                intent.putExtra("tag", task);
                intent.putExtra("projectId", ProjectId);
                startActivity(intent);        		
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_task_list, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_create_task:
            	Intent intent = new Intent();
                intent.setClass(TaskListActivity.this, TaskDetailActivity.class);
            	intent.putExtra("projectId", ProjectId);
                startActivity(intent);
                this.finish();
                break;
            case R.id.menu_delete_task:
                Toast.makeText(this, "删除", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_refresh_task_list:
                loadTasks();
                break;
        }
        return false;
    }
	
	private void loadTasks() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				initializeActivity();
			}
		}).start();
	}

	private void initializeActivity() {
		TaskVisitor tv = new TaskVisitor();
		List<Task> tasks = tv.getTasks(ProjectId);
		if(tasks == null) {
			String errmsg = getString(R.string.task_list_fail_to_retrieve_tasks);
			Toast.makeText(TaskListActivity.this, errmsg, Toast.LENGTH_LONG).show();
		}

        Message msg = new Message();
        msg.what = WHAT_INIT_TASK_LIST;
        msg.obj = tasks;
        handler.sendMessage(msg);
	}
    
    private void refreshTaskList(Message msg) {
        @SuppressWarnings("unchecked")
		List<Task> tasks = (List<Task>)msg.obj;
        List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < tasks.size(); i++) {
        	Task task = tasks.get(i);            	
        	
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", task.getTaskId());
            map.put("task_item_image_icon", R.drawable.ic_task);
            map.put("task_item_text_content", task.getTaskContent());
            map.put("task_item_text_status", task.getStatusString());
            map.put("task_item_text_update_time", task.getUpdateTimeString());
            map.put("tag", task);
            contents.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                contents,
                R.layout.layout_task_item,
                new String[]{"task_item_image_icon",
                			 "task_item_text_content",
                			 "task_item_text_status",
                			 "task_item_text_update_time"},
                new int[]{R.id.task_item_image_icon,
                		   R.id.task_item_text_content,
                		   R.id.task_item_text_status,
                		   R.id.task_item_text_update_time}
        );

        ListView lsvTasks = (ListView)findViewById(R.id.task_list);
        lsvTasks.setAdapter(adapter);
    }
	
	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	switch(msg.what) {
        	case WHAT_INIT_TASK_LIST:
        		refreshTaskList(msg);
        		break;
        	}
        }
    };
}