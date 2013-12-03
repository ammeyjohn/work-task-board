package com.fatboy.microtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fatboy.microtask.models.Global;
import com.fatboy.microtask.models.Task;
import com.fatboy.microtask.visitors.TaskVisitor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TaskListActivity extends Activity {

	final static int WHAT_INIT_TASK_LIST = 1;
	private int _ProjectId = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		
		// Get relative project id.
		Intent intent = getIntent();
		_ProjectId = intent.getIntExtra("projectId", 0);
		
		// Initialize the task list.
		loadTasks(true);
		
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
                intent.putExtra("projectId", _ProjectId);
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
            	intent.putExtra("projectId", _ProjectId);
                startActivity(intent);
                this.finish();
                break;
            case R.id.menu_uncomplete_task:
            	loadTasks(false);
            	this.setTitle(getText(R.string.context_menu_uncomplete_task));
            	break;
            case R.id.menu_refresh_task_list:
                loadTasks(true);
                break;
        }
        return false;
    }
	
	private void loadTasks(final Boolean showAll) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				initializeActivity(showAll);
			}
		}).start();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	doBack();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
    
    private void doBack() {
        Intent intent = new Intent();     
        intent.setClass(TaskListActivity.this, ProjectListActivity.class);
        intent.putExtra("projectId", _ProjectId);
        startActivity(intent);  
        this.finish();
    }

	private void initializeActivity(Boolean showAll) {
		Global g = (Global)this.getApplicationContext();
		int userId = g.getCurrentUser().getUserId();

		String status = null;
		if(!showAll) {
			status = "1,2";
		}
		
		TaskVisitor v = new TaskVisitor();
		List<Task> tasks = v.getTasks(_ProjectId, userId, status);
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
            map.put("task_item_image_icon", task.getStatusIcon());
            map.put("task_item_text_content", task.getTaskContent());
            map.put("task_item_text_status", task.getStatusString());
            map.put("task_item_text_expect_date", task.getExpectDateString());
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
                			 "task_item_text_expect_date"},
                new int[]{R.id.task_item_image_icon,
                		   R.id.task_item_text_content,
                		   R.id.task_item_text_status,
                		   R.id.task_item_text_expect_date}
        );

        ListView lsvTasks = (ListView)findViewById(R.id.task_list);
        lsvTasks.setAdapter(adapter);
        
        lsvTasks.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.setHeaderTitle(R.string.task_list_context_menu_header_title);
				menu.add(0, 1, Menu.NONE, R.string.project_list_context_menu0_name);
			}		
		});
    }
    
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		ListView lsvTasks = (ListView)findViewById(R.id.task_list);

		@SuppressWarnings("unchecked")
		final Map<String, Object> map = (Map<String, Object>)lsvTasks.getAdapter().getItem(info.position);
		switch(item.getItemId()) {
		case Menu.FIRST:
			// Show dialog to confirm deleting project.
			AlertDialog.Builder builder = new Builder(TaskListActivity.this);
			builder.setMessage(getString(R.string.task_list_dialog_message));
			builder.setTitle(getString(R.string.task_list_dialog_title));
			builder.setPositiveButton(getString(R.string.task_list_dialog_ok_button), 
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							deleteTask(Integer.parseInt(map.get("id").toString()));
						}
					});
			builder.setNegativeButton(getString(R.string.project_list_dialog_cancel_button), null);
			builder.create().show();
			break;
		}
		return false; 
	}
	
	private void deleteTask(final int taskId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				TaskVisitor v = new TaskVisitor();
				Boolean r = v.deleteTask(taskId);
				
				if (r) {
					loadTasks(true);
				} else {
					String errmsg = getString(R.string.task_list_fail_to_del_task);
					Toast.makeText(TaskListActivity.this, errmsg, Toast.LENGTH_LONG).show();
				}
			}
		}).start();
	}
	
	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	switch(msg.what) {
        	case WHAT_INIT_TASK_LIST:
        		refreshTaskList(msg);
        		TaskListActivity.this.setTitle(getText(R.string.task_list_title));
        		break;
        	}
        }
    };
}