package com.fatboy.microtask;

import java.text.SimpleDateFormat;

import com.fatboy.microtask.models.Task;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
            
public class TaskDetailActivity extends Activity {

	final static int WHAT_INIT_TASK = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail);
		
		// Get relative task id.
		Intent intent = getIntent();
		Task task = (Task)intent.getSerializableExtra("task");
		initializeActivity(task);
	}
    
    private void initializeActivity(Task task) {
				
		String id = Integer.toString(task.getTaskId());
		((TextView)this.findViewById(R.id.task_id)).setText(id);

		((TextView)this.findViewById(R.id.task_status)).setText("New");

		String content = task.getTaskContent();
		if (content.length() >= 22) {
			content = content.substring(0, 20);
			content += "...";
		}
		((TextView)this.findViewById(R.id.task_content)).setText(content);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = formatter.format(task.getCreateTime());
		String updateTime = formatter.format(task.getUpdateTime());
		((TextView)this.findViewById(R.id.create_time)).setText(createTime);
		((TextView)this.findViewById(R.id.expected_complete_date)).setText(updateTime);
		
    }

}
