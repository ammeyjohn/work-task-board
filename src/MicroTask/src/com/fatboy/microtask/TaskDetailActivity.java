package com.fatboy.microtask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fatboy.microtask.models.Global;
import com.fatboy.microtask.models.Task;
import com.fatboy.microtask.models.User;
import com.fatboy.microtask.utils.Utils;
import com.fatboy.microtask.visitors.TaskVisitor;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
            
public class TaskDetailActivity extends Activity {

	final TaskDetailActivity That = this;
	final static int WHAT_INIT_USER = 1;
	final static int DATE_DIALOG_ID = 1;
	
	private Task _Task = null;
	private int _ProjectId = -1;
	private Boolean _InUpdateMode = false;
	
	private TextView mIdView;
	private TextView mStatusView;
	private TextView mContentView;
	private TextView mCreateUserView;
	private TextView mAssignUserView;
	private TextView mCreateTimeView;
	private TextView mUpdateTimeView;
	private TextView mExpectDateView;
	private TextView mPriorityView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail);
		
		// Retrieve all components.
		mIdView = (TextView)findViewById(R.id.task_detail_label_task_id);
		mStatusView = (TextView)findViewById(R.id.task_detail_label_task_status);
		mContentView = (TextView)findViewById(R.id.task_detail_label_task_content);
		mCreateUserView = (TextView)findViewById(R.id.task_detail_label_create_user);
		mAssignUserView = (TextView)findViewById(R.id.task_detail_label_assign_user);
		mCreateTimeView = (TextView)findViewById(R.id.task_detail_label_create_time);
		mUpdateTimeView = (TextView)findViewById(R.id.task_detail_label_update_time);
		mExpectDateView = (TextView)findViewById(R.id.task_detail_label_except_date);
		mPriorityView = (TextView)findViewById(R.id.task_detail_label_task_priority);
		
		// Initialize the action bar.
		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.detail_titlebar);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.show();
		
		// Get relative task id.
		Intent intent = getIntent();
		_Task = (Task)intent.getSerializableExtra("tag");
		_ProjectId = intent.getIntExtra("projectId", _ProjectId);
		_InUpdateMode = (_Task != null);
		
		// To initialize activity by given task.
		initializeActivity();
		
   		// Status only can be change in Update Mode.
		if(_InUpdateMode) {
			// Binding click event to table rows.
			bindingStatusRowClickEvent();
		}
		bindingContentRowClickEvent();
		bindingExpectDateRowClickEvent();
		bindingPriorityRowClickEvent();
		
		// Binding user event
		TableRow assignUserRow = (TableRow)this.findViewById(R.id.tr_task_detail_assign_user);
		bindingUserRowClickEvent(assignUserRow, mAssignUserView, getString(R.string.task_detail_dialog_title_assign_user));
		
		// Binding click event to button on title bar.
		Button btnSave = (Button)findViewById(R.id.action_titlebar_save);
		btnSave.setOnClickListener(new ButtonSaveListener());

		Button btnBack = (Button)findViewById(R.id.action_titlebar_back);
		btnBack.setOnClickListener(new ButtonBackListener());		
	}
	
	private void bindingStatusRowClickEvent() {
		TableRow row = (TableRow)findViewById(R.id.tr_task_detail_task_status);
		row.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				final SingleChoiceClick onChoiceClick = new SingleChoiceClick();
				final String[] status = Utils.getStatusText();
				
				int index = 0;
				Object tag = mStatusView.getTag();
				if(tag != null) {
					index = Integer.parseInt(tag.toString())-1;
				}
				AlertDialog.Builder builder = new Builder(TaskDetailActivity.this);
				builder.setTitle(getString(R.string.task_detail_dialog_title_task_status));
				builder.setSingleChoiceItems(status, index, onChoiceClick);				
				builder.setNegativeButton(getString(R.string.task_detail_dialog_cancel_button), null);
				builder.setPositiveButton(getString(R.string.task_detail_dialog_ok_button), 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								int index = onChoiceClick.ChoiceIndex;
								mStatusView.setText(status[index]);
								mStatusView.setTag(index+1);
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
				final EditText txtContent = new EditText(That);
				txtContent.setHeight(300);
				txtContent.setGravity(Gravity.LEFT | Gravity.TOP);
				txtContent.setEnabled(true);
				
				Object tag = mContentView.getTag();
				if(tag != null) {
					txtContent.setText(tag.toString());
				}
				
				AlertDialog.Builder builder = new Builder(TaskDetailActivity.this);
				builder.setTitle(getString(R.string.task_detail_dialog_title_task_content));
				builder.setView(txtContent);
				builder.setNegativeButton(getString(R.string.task_detail_dialog_cancel_button), null);
				builder.setPositiveButton(getString(R.string.task_detail_dialog_ok_button), 
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String content = txtContent.getText().toString();
								mContentView.setTag(content);
								mContentView.setText(Utils.getNeglectString(25, content));
							}
						});
				builder.create().show();	
			}
			
		});
	}

	private void bindingUserRowClickEvent(final TableRow row, final TextView view, final String title) {		
		row.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Global g = (Global)That.getApplicationContext();
				final List<User> users = g.getUsers();
			
				User tag = (User)view.getTag();

				int index = 0;
				final SingleChoiceClick onChoiceClick = new SingleChoiceClick();
				final String[] names = new String[users.size()];
				for(int i = 0; i < users.size(); i++) {
					User user = users.get(i);
					names[i] = user.getUserName();
					if(tag.getUserId() == user.getUserId()){
						index = i;
					}
				}
				AlertDialog.Builder builder = new Builder(TaskDetailActivity.this);
				builder.setTitle(title);
				builder.setSingleChoiceItems(names, index, onChoiceClick);				
				builder.setNegativeButton(getString(R.string.task_detail_dialog_cancel_button), null);
				builder.setPositiveButton(getString(R.string.task_detail_dialog_ok_button), 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								int index = onChoiceClick.ChoiceIndex;
								User user = users.get(index);
								view.setText(user.getUserName());
								view.setTag(user);
							}
						});
				builder.create().show();	
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	private void bindingExpectDateRowClickEvent() {
		TableRow row = (TableRow)findViewById(R.id.tr_task_detail_expect_date);
		row.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
	        	Date expectDate = null;
	        	if(_Task != null) expectDate = _Task.getUpdateTime();
	        	else expectDate = new Date();
				
	        	mExpectDateView.setTag(expectDate);
				That.showDialog(DATE_DIALOG_ID);	
			}
		});
	}
	
	private void bindingPriorityRowClickEvent() {
		TableRow row = (TableRow)findViewById(R.id.tr_task_detail_task_priority);
		row.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				final EditText txtPriority = new EditText(That);
				txtPriority.setText(mPriorityView.getText());
				txtPriority.setInputType(InputType.TYPE_CLASS_NUMBER);
				
				AlertDialog.Builder builder = new Builder(TaskDetailActivity.this);
				builder.setTitle(getString(R.string.task_detail_dialog_title_task_priority));
				builder.setView(txtPriority);
				builder.setNegativeButton(getString(R.string.task_detail_dialog_cancel_button), null);
				builder.setPositiveButton(getString(R.string.task_detail_dialog_ok_button), 
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								int priority = Integer.parseInt(txtPriority.getText().toString());
								mPriorityView.setText(txtPriority.getText());
								mPriorityView.setTag(priority);
							}
						});
				builder.create().show();	
			}
			
		});
	}
	
    private DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener() {  
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  
        	Calendar c = Calendar.getInstance();
        	c.set(Calendar.YEAR, year);
        	c.set(Calendar.MONTH, monthOfYear);
        	c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        	Date expectDate = c.getTime();
        	
        	mExpectDateView.setText(Utils.getDateString(expectDate));
        	mExpectDateView.setTag(expectDate);
        }  
    };  
    
    @Override  
    protected Dialog onCreateDialog(int id) {  
       switch (id) {  
        case DATE_DIALOG_ID:  
        	Calendar c = Calendar.getInstance();
        	c.setTime((Date)mExpectDateView.getTag());
            return new DatePickerDialog(this,mDateSetListener, 
            		c.get(Calendar.YEAR),
            		c.get(Calendar.MONTH),	
            		c.get(Calendar.DAY_OF_MONTH));  
        }  
        return null;  
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
        intent.setClass(TaskDetailActivity.this, TaskListActivity.class);
        intent.putExtra("projectId", _ProjectId);
        startActivity(intent);  
        TaskDetailActivity.this.finish();
    }
    	
	class ButtonBackListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			doBack();
		}
		
	}

	class ButtonSaveListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
    		int status = Integer.parseInt(mStatusView.getTag().toString());
    		int priority = Integer.parseInt(mPriorityView.getTag().toString());
    		String content = mContentView.getTag().toString();
    		Date expectDate =(Date)mExpectDateView.getTag();
    		User assignUser = (User)mAssignUserView.getTag();
			
    		Global g = (Global)That.getApplicationContext();
    		User createUser = g.getCurrentUser();
			if(_Task == null) {
				_Task = new Task();
				_Task.setTaskType(1);
				_Task.setProjectId(_ProjectId);
				_Task.setUserId(createUser.getUserId());
			}
			_Task.setStatus(status);
			_Task.setPriority(priority);
			_Task.setAssignUser(assignUser.getUserId());
			_Task.setTaskContent(content);
			_Task.setExpectDate(expectDate);
			
			createTask(_Task);
		}
		
	}
	
	private void createTask(final Task task) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Boolean result = true;

				TaskVisitor v = new TaskVisitor();
				if (_InUpdateMode) {
					result = v.modifyTask(task);
				} else {
					result = (v.createTask(task) > 0);
				}

				if(!result) {
					String errmsg = getString(R.string.task_detail_fail_to_save_task);
					Toast.makeText(TaskDetailActivity.this, errmsg, Toast.LENGTH_SHORT).show();
				}
				
                Intent intent = new Intent();     
                intent.setClass(TaskDetailActivity.this, TaskListActivity.class);
                intent.putExtra("projectId", _ProjectId);
                startActivity(intent);  
                That.finish();
			}
		}).start();
	}
    
	class SingleChoiceClick implements DialogInterface.OnClickListener {
		public int ChoiceIndex = 0;
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			this.ChoiceIndex = which;
		}
	};
	
    private void initializeActivity() {
    	if(!_InUpdateMode) {
    		mIdView.setText("");
    		
    		mStatusView.setText("");
    		mStatusView.setTag(1);
    		
    		mContentView.setText("");
    		mContentView.setTag("");
    		
    		Date now = new Date();
    		mCreateTimeView.setText(Utils.getDateTimeString(now));
    		mCreateTimeView.setTag(now);
    		mUpdateTimeView.setText(Utils.getDateTimeString(now));
    		mUpdateTimeView.setTag(now);
    		mExpectDateView.setText(Utils.getDateTimeString(now));
    		mExpectDateView.setTag(now);
    		
    		Global g = (Global)this.getApplicationContext();
    		User user = g.getCurrentUser();
    		mCreateUserView.setText(user.getUserName());
    		mCreateUserView.setTag(user);
    		mAssignUserView.setText(user.getUserName());
    		mAssignUserView.setTag(user);
    		
    		mPriorityView.setText("1");
    		mPriorityView.setTag(1);
    		
    	} else {
    		mIdView.setText(Integer.toString(_Task.getTaskId()));
    		
    		mStatusView.setText(_Task.getStatusString());
    		mStatusView.setTag(_Task.getStatus());
    		
    		mContentView.setText(Utils.getNeglectString(25, _Task.getTaskContent()));
    		mContentView.setTag(_Task.getTaskContent());
    		
    		Date createTime = _Task.getCreateTime();
    		mCreateTimeView.setText(Utils.getDateTimeString(createTime));
    		mCreateTimeView.setTag(createTime);

    		Date updateTime = _Task.getUpdateTime();
    		mUpdateTimeView.setText(Utils.getDateTimeString(updateTime));
    		mUpdateTimeView.setTag(updateTime);

    		Date assignTime = _Task.getExpectDate();
    		mExpectDateView.setText(Utils.getDateTimeString(assignTime));
    		mExpectDateView.setTag(assignTime);
    		
    		Global g = (Global)this.getApplicationContext();
    		User createUser = Utils.findUserById(g.getUsers(), _Task.getUserId());
    		mCreateUserView.setText(createUser.getUserName());
    		mCreateUserView.setTag(createUser);

    		User assignUser = Utils.findUserById(g.getUsers(), _Task.getAssignUser());
    		mAssignUserView.setText(assignUser.getUserName());
    		mAssignUserView.setTag(assignUser);
    		
    		mPriorityView.setText(Integer.toString(_Task.getPriority()));
    		mPriorityView.setTag(_Task.getPriority());
    		
    	}

    }
    
}
