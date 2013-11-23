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
	final static int WHAT_INIT_TASK = 2;
	private int ProjectId = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasklist);
		
		// Get relative project id.
		Intent intent = getIntent();
		ProjectId = intent.getIntExtra("id", 0);
		
		// Initialize the task list.
		loadTasks();
		
		// Attach events to ListView
		ListView lsvTasks = (ListView)findViewById(R.id.listTasks);
		lsvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
		    @Override  
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  
		        @SuppressWarnings("unchecked")  		       
		        HashMap<String,Object> map = (HashMap<String, Object>) parent.getItemAtPosition(position);  
		        
		        // Load clicked task.
		        loadTask(Integer.parseInt(map.get("id").toString()));		        	    
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_tasklist, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_new_task:
                Toast.makeText(this, "新建", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_del_task:
                Toast.makeText(this, "删除", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_modify_task:
                Toast.makeText(this, "修改", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_detail_task:
                Toast.makeText(this, "详细", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_refresh_tasklist:
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
	
	private void loadTask(final int taskId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				initializeActivity(taskId);
			}
		}).start();
	}

	private void initializeActivity(int taskId) {		
		TaskVisitor v = new TaskVisitor();
		Task task = v.getTask(taskId);
		if(task == null) {
			Toast.makeText(this, "Failed to retrieve task.", Toast.LENGTH_LONG).show();
		}

        Message msg = new Message();
        msg.what = WHAT_INIT_TASK;
        msg.obj = task;
        handler.sendMessage(msg);
	}

	private void initializeActivity() {		
		TaskVisitor v = new TaskVisitor();
		List<Task> tasks = v.getTasks(ProjectId);
		if(tasks == null) {
			Toast.makeText(TaskListActivity.this, "Failed to retrieve task list.", Toast.LENGTH_LONG).show();
		}

        Message msg = new Message();
        msg.what = WHAT_INIT_TASK_LIST;
        msg.obj = tasks;
        handler.sendMessage(msg);
	}

    
	final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	switch(msg.what) {
        	case WHAT_INIT_TASK_LIST:
        		RefreshTaskList(msg);
        		break;
        	case WHAT_INIT_TASK:
		        // Start task activity to show relative tasks.
        		Task task = (Task)msg.obj;
                Intent intent = new Intent();     
                intent.setClass(TaskListActivity.this, TaskDetailActivity.class);
                intent.putExtra("task", task);
                startActivity(intent); 
        		break;
        	}
        }
        
        private void RefreshTaskList(Message msg) {
            @SuppressWarnings("unchecked")
			List<Task> tasks = (List<Task>)msg.obj;
            List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
            for(int i = 0; i < tasks.size(); i++) {
            	Task task = tasks.get(i);            	
            	
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", task.getTaskId());
                map.put("img", R.drawable.ic_task);
                map.put("txtContent", task.getTaskContent());
                map.put("txtStatus", task.getStatus());
                map.put("txtUpdateTime", task.getUpdateTimeString());
                contents.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(
                    TaskListActivity.this,
                    contents,
                    R.layout.layout_taskitem,
                    new String[]{"img","txtContent","txtStatus","txtUpdateTime"},
                    new int[]{R.id.img,R.id.txtContent,R.id.txtStatus,R.id.txtUpdateTime}
            );

            ListView lsvTasks = (ListView)findViewById(R.id.listTasks);
            lsvTasks.setAdapter(adapter);
        }
    };
}
