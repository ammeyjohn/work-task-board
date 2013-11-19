package com.fatboy.microtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TaskListActivity extends Activity {

	final private Activity Outer = this;
	
	public final static int WHAT_INIT_TASK_LIST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasklist);
		
		// Initialize the task list.
		LoadTasks();
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
                LoadTasks();
                break;
        }
        return false;
    }
	
	private void LoadTasks() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				initinalActivity();
			}
		}).start();
	}

	private void initinalActivity() {		
		TaskVisitor v = new TaskVisitor();
		List<Task> tasks = v.getTasks();
		if(tasks == null) {
			Toast.makeText(Outer, "Failed to retrieve task list.", Toast.LENGTH_LONG).show();
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
        	}
        }
        
        private void RefreshTaskList(Message msg) {
            @SuppressWarnings("unchecked")
			List<Task> tasks = (List<Task>)msg.obj;
            List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
            for(int i = 0; i < tasks.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("txtItemText", tasks.get(i).getTaskContent());
                contents.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(
                    Outer,
                    contents,
                    R.layout.layout_taskitem,
                    new String[]{"txtItemText"},
                    new int[]{R.id.txtItem}
            );

            ListView lsvTasks = (ListView)findViewById(R.id.listTasks);
            lsvTasks.setAdapter(adapter);
            
            Toast.makeText(Outer, "Task list has been refreshed.", Toast.LENGTH_LONG).show();
        }
    };
}
