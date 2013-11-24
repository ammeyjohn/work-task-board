package com.fatboy.microtask;

import com.fatboy.microtask.models.Task;
import com.fatboy.microtask.utils.Utils;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
            
public class TaskDetailActivity extends Activity {

	final static int WHAT_INIT_TASK = 1;
	private Task _Task = null;
	private int _ProjectId = -1;
	private Boolean _InUpdateMode = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail);
		
		// Initialize the action bar.
		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.detail_titlebar);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.show();
		
		// Binding click event to button on title bar.
		Button btn_save = (Button)findViewById(R.id.action_titlebar_save);
		btn_save.setOnClickListener(new ButtonSaveListener());

		Button btn_back = (Button)findViewById(R.id.action_titlebar_back);
		btn_back.setOnClickListener(new ButtonBackListener());
		
		// Get relative task id.
		Intent intent = getIntent();
		_Task = (Task)intent.getSerializableExtra("tag");
		_ProjectId = intent.getIntExtra("id", _ProjectId);
		_InUpdateMode = (_Task != null);

		// To initialize activity by given task.
		initializeActivity(_Task);
		
		// Binding click event to table rows.
		bindingStatusRowClickEvent();
		bindingContentRowClickEvent();
	}
	
	private void bindingStatusRowClickEvent() {
		TableRow row = (TableRow)findViewById(R.id.tr_task_detail_task_status);
		row.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				class SingleChoiceClick implements DialogInterface.OnClickListener {
					public int ChoiceIndex = 0;
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						this.ChoiceIndex = which;
					}
					
				};
				
				TaskDetailActivity that = TaskDetailActivity.this;
				final String[] status = new String[]{ "已创建", "已分派", "已完成", "已确定" };
				final TextView lblStatus = (TextView)that.findViewById(R.id.task_detail_label_task_status);
				final SingleChoiceClick onChoiceClick = new SingleChoiceClick();
				
				int index = 0;
				Object tag = lblStatus.getTag();
				if(tag != null) index = Integer.parseInt(tag.toString());
				AlertDialog.Builder builder = new Builder(TaskDetailActivity.this);
				builder.setTitle(getString(R.string.task_detail_dialog_title_task_status));
				builder.setSingleChoiceItems(status, index, onChoiceClick);				
				builder.setNegativeButton(getString(R.string.task_detail_dialog_cancel_button), null);
				builder.setPositiveButton(getString(R.string.task_detail_dialog_ok_button), 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								int index = onChoiceClick.ChoiceIndex;
								lblStatus.setText(status[index]);
								lblStatus.setTag(index);
							}
						});
				builder.create().show();	
			}
			
		});
	}
	
	private void bindingContentRowClickEvent() {
		TableRow row = (TableRow)findViewById(R.id.tr_task_detail_task_content);
		row.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				TaskDetailActivity that = TaskDetailActivity.this;
				final TextView lblContent = (TextView)that.findViewById(R.id.task_detail_label_task_content);
				final EditText txtContent = new EditText(TaskDetailActivity.this);
				txtContent.setHeight(300);
				txtContent.setGravity(Gravity.LEFT | Gravity.TOP);
				txtContent.setEnabled(true);
				
				Object tag = lblContent.getTag();
				if(tag != null) {
					txtContent.setText(Utils.getNeglectString(15, tag.toString()));
				}
				
				AlertDialog.Builder builder = new Builder(TaskDetailActivity.this);
				builder.setTitle(getString(R.string.task_detail_dialog_title_task_content));
				builder.setView(txtContent);
				builder.setPositiveButton(getString(R.string.task_detail_dialog_ok_button), 
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String content = txtContent.getText().toString();
								lblContent.setTag(content);
								lblContent.setText(Utils.getNeglectString(15, content));
							}
						});
				builder.setNegativeButton(getString(R.string.task_detail_dialog_cancel_button), null);
				builder.create().show();	
			}
			
		});
	}
	
	class ButtonBackListener implements OnClickListener {

		@Override
		public void onClick(View v) {
            Intent intent = new Intent();     
            intent.setClass(TaskDetailActivity.this, TaskListActivity.class);
            intent.putExtra("id", _ProjectId);
            startActivity(intent);  
            TaskDetailActivity.this.finish();
		}
		
	}

	class ButtonSaveListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {

		}
		
	}
    
    private void initializeActivity(Task task) {
    	if(!_InUpdateMode) {
    		((TextView)findViewById(R.id.task_detail_label_task_id)).setText("");
    		((TextView)findViewById(R.id.task_detail_label_task_status)).setText("");
    		((TextView)findViewById(R.id.task_detail_label_task_content)).setText("");
    		((TextView)findViewById(R.id.task_detail_label_create_user)).setText("");
    		((TextView)findViewById(R.id.task_detail_label_assign_user)).setText("");
    		((TextView)findViewById(R.id.task_detail_label_create_time)).setText("");
    		((TextView)findViewById(R.id.task_detail_label_update_time)).setText("");
    		((TextView)findViewById(R.id.task_detail_label_task_priority)).setText("");
    		((TextView)findViewById(R.id.task_detail_label_except_date)).setText("");
    		
    	} else {
    		((TextView)findViewById(R.id.task_detail_label_task_id)).setText(Integer.toString(_Task.getTaskId()));
    		((TextView)findViewById(R.id.task_detail_label_create_user)).setText(Integer.toString(_Task.getUserId()));
    		((TextView)findViewById(R.id.task_detail_label_assign_user)).setText(Integer.toString(_Task.getUserId()));
    		((TextView)findViewById(R.id.task_detail_label_create_time)).setText(_Task.getCreateTimeString());
    		((TextView)findViewById(R.id.task_detail_label_update_time)).setText(_Task.getUpdateTimeString());
    		((TextView)findViewById(R.id.task_detail_label_task_priority)).setText("1");
    		((TextView)findViewById(R.id.task_detail_label_except_date)).setText(_Task.getUpdateTimeString());
    		
    		TextView txtStatus = (TextView)findViewById(R.id.task_detail_label_task_status);
    		txtStatus.setTag(_Task.getStatus());
			txtStatus.setText(_Task.getStatusString());

    		String content = _Task.getTaskContent();
    		TextView text_cont = (TextView)findViewById(R.id.task_detail_label_task_content);
    		text_cont.setTag(content);
    		text_cont.setText(Utils.getNeglectString(15, content));

    	}
    }

}
