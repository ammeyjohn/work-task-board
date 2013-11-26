package com.fatboy.microtask;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fatboy.microtask.models.Task;
import com.fatboy.microtask.models.User;
import com.fatboy.microtask.utils.Utils;
import com.fatboy.microtask.visitors.TaskVisitor;
import com.fatboy.microtask.visitors.UserVisitor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
            
public class TaskDetailActivity extends Activity {

	final static int WHAT_INIT_USER = 1;
	final static int DATE_DIALOG_ID = 1;
	private Task _Task = null;
	private int _ProjectId = -1;
	private int _UserId = 1;
	private Boolean _InUpdateMode = false;
	private List<User> _Users = null;
	
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
		
		// Get relative task id.
		Intent intent = getIntent();
		_Task = (Task)intent.getSerializableExtra("tag");
		_ProjectId = intent.getIntExtra("projectId", _ProjectId);
		_UserId = intent.getIntExtra("userId", _UserId);
		_InUpdateMode = (_Task != null);
		
		// Binding click event to button on title bar.
		Button btn_save = (Button)findViewById(R.id.action_titlebar_save);
		btn_save.setOnClickListener(new ButtonSaveListener());

		Button btn_back = (Button)findViewById(R.id.action_titlebar_back);
		btn_back.setOnClickListener(new ButtonBackListener());
		
		// Binding click event to table rows.
		bindingStatusRowClickEvent();
		bindingContentRowClickEvent();
		bindingExpectDateRowClickEvent();
		bindingPriorityRowClickEvent();
		
		// Do user loading.
		loadUsers();
	}
	
	private void loadUsers() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				UserVisitor v = new UserVisitor();
				_Users = v.getUsers();
				
				if(_Users != null) {
			        Message msg = new Message();
			        msg.what = WHAT_INIT_USER;
			        handler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	private void bindingStatusRowClickEvent() {
		TableRow row = (TableRow)findViewById(R.id.tr_task_detail_task_status);
		row.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
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

	private void bindingUserRowClickEvent(final TableRow row, final TextView view, final String title) {		
		row.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String[] names = new String[_Users.size()];
				for(int i = 0; i < _Users.size(); i++) {
					User user = _Users.get(i);
					names[i] = user.getUserName();
				}
				final SingleChoiceClick onChoiceClick = new SingleChoiceClick();
				
				int index = 0;
				User tag = (User)view.getTag();
				if(tag != null) {
					for(int i = 0; i < _Users.size(); i++) {
						User user = _Users.get(i);
						if(user.getUserId() == tag.getUserId()) {
							index = i;
							break;
						}
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
								User user = _Users.get(index);
								view.setText(user.getUserName());
								view.setTag(user);
							}
						});
				builder.create().show();	
			}
		});
	}
	
	private void bindingExpectDateRowClickEvent() {
		TableRow row = (TableRow)findViewById(R.id.tr_task_detail_expect_date);
		row.setOnClickListener(new OnClickListener(){
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
	        	Date expectDate = null;
	        	if(_Task != null) expectDate = _Task.getUpdateTime();
	        	else expectDate = new Date();
				
	        	TaskDetailActivity that = TaskDetailActivity.this;
	        	TextView lblExpectDate = (TextView)that.findViewById(R.id.task_detail_label_except_date);
	        	lblExpectDate.setTag(expectDate);
				TaskDetailActivity.this.showDialog(DATE_DIALOG_ID);	
			}
		});
	}
	
	private void bindingPriorityRowClickEvent() {
		TableRow row = (TableRow)findViewById(R.id.tr_task_detail_task_priority);
		row.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				TaskDetailActivity that = TaskDetailActivity.this;
				final TextView lblPriority = (TextView)that.findViewById(R.id.task_detail_label_task_priority);
				final EditText txtPriority = new EditText(TaskDetailActivity.this);
				txtPriority.setText(txtPriority.getText());
				txtPriority.setInputType(InputType.TYPE_CLASS_NUMBER);
				
				AlertDialog.Builder builder = new Builder(TaskDetailActivity.this);
				builder.setTitle(getString(R.string.task_detail_dialog_title_task_priority));
				builder.setView(txtPriority);
				builder.setPositiveButton(getString(R.string.task_detail_dialog_ok_button), 
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								lblPriority.setText(txtPriority.getText());
							}
						});
				builder.setNegativeButton(getString(R.string.task_detail_dialog_cancel_button), null);
				builder.create().show();	
			}
			
		});
	}
	
    private DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener() {  
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  
        	Calendar c = Calendar.getInstance();
        	c.set(Calendar.YEAR, year);
        	c.set(Calendar.MONTH, monthOfYear+1);
        	c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        	Date expectDate = c.getTime();
        	
        	TaskDetailActivity that = TaskDetailActivity.this;
        	TextView lblExpectDate = (TextView)that.findViewById(R.id.task_detail_label_except_date);
        	lblExpectDate.setTag(expectDate);
        	lblExpectDate.setText(Utils.getDateString(expectDate));
        }  
    };  
    
    @Override  
    protected Dialog onCreateDialog(int id) {  
       switch (id) {  
        case DATE_DIALOG_ID:  
        	TextView lblExpectDate = (TextView)this.findViewById(R.id.task_detail_label_except_date);
        	Calendar c = Calendar.getInstance();
        	c.setTime((Date)lblExpectDate.getTag());
            return new DatePickerDialog(this,mDateSetListener, 
            		c.get(Calendar.YEAR),
            		c.get(Calendar.MONTH)-1,
            		c.get(Calendar.DAY_OF_MONTH));  
        }  
        return null;  
    }  
	
	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	switch(msg.what) {
        	case WHAT_INIT_USER:
				// Refresh create user or assign user.
				refreshUsers();

				// To initialize activity by given task.
				initializeActivity();
				
				TaskDetailActivity that = TaskDetailActivity.this;
				TableRow rowAssignUser = (TableRow)findViewById(R.id.tr_task_detail_assign_user);
				TextView lblAssignUser = (TextView)that.findViewById(R.id.task_detail_label_assign_user);
				String title1 = getString(R.string.task_detail_dialog_title_assign_user);
				bindingUserRowClickEvent(rowAssignUser, lblAssignUser, title1);
        		break;
        	}
        }
    };
		
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

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onClick(View arg0) {
			
    		int status = Integer.parseInt(((TextView)findViewById(R.id.task_detail_label_task_status)).getTag().toString());
    		int priority = Integer.parseInt(((TextView)findViewById(R.id.task_detail_label_task_priority)).getText().toString());
    		Date expect_date =(Date)((TextView)findViewById(R.id.task_detail_label_except_date)).getTag();
    		String content = ((TextView)findViewById(R.id.task_detail_label_task_content)).getTag().toString();
    		
    		User assign_user = (User)((TextView)findViewById(R.id.task_detail_label_assign_user)).getTag();
    		int assign_user_id = assign_user.getUserId();
			
			if(_Task == null) {
				_Task = new Task();
			}
			_Task.setStatus(status);
			_Task.setAssignUser(assign_user_id);
			_Task.setPriority(priority);
			_Task.setTaskContent(content);
			_Task.setExpectDate(expect_date);
			_Task.setProjectId(_ProjectId);
			_Task.setUserId(_UserId);
			
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
                intent.putExtra("id", _ProjectId);
                intent.putExtra("userId", _UserId);
                startActivity(intent);  
                TaskDetailActivity.this.finish();
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
    		((TextView)findViewById(R.id.task_detail_label_task_id)).setText("");
    		((TextView)findViewById(R.id.task_detail_label_task_status)).setText("待创建");
    		((TextView)findViewById(R.id.task_detail_label_task_status)).setTag(1);
    		((TextView)findViewById(R.id.task_detail_label_task_content)).setText("");
    		
    		User user = findUserById(_UserId);
    		if(user != null) {
    			((TextView)findViewById(R.id.task_detail_label_create_user)).setText(user.getUserName());
    			((TextView)findViewById(R.id.task_detail_label_create_user)).setTag(user);
    			((TextView)findViewById(R.id.task_detail_label_assign_user)).setText(user.getUserName());
    			((TextView)findViewById(R.id.task_detail_label_assign_user)).setTag(user);
    		}
    		
    		Date now = new Date();
    		((TextView)findViewById(R.id.task_detail_label_create_time)).setText(Utils.getDateString(now));
    		((TextView)findViewById(R.id.task_detail_label_create_time)).setTag(now);
    		((TextView)findViewById(R.id.task_detail_label_update_time)).setText(Utils.getDateString(now));
    		((TextView)findViewById(R.id.task_detail_label_update_time)).setTag(now);
    		((TextView)findViewById(R.id.task_detail_label_except_date)).setText(Utils.getDateString(now));
    		((TextView)findViewById(R.id.task_detail_label_except_date)).setTag(now);
    		((TextView)findViewById(R.id.task_detail_label_task_priority)).setText("1");
    		
    	} else {
    		((TextView)findViewById(R.id.task_detail_label_task_id)).setText(Integer.toString(_Task.getTaskId()));
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

    private void refreshUsers() {
    	if(_InUpdateMode) {
    		User createUser = findUserById(_Task.getUserId());
    		if(createUser != null) {
    			((TextView)findViewById(R.id.task_detail_label_create_user)).setText(createUser.getUserName());
    			((TextView)findViewById(R.id.task_detail_label_create_user)).setTag(createUser);
    		}
    		
    		User assignUser = findUserById(_Task.getAssignUser());
    		if(assignUser != null) {
    			((TextView)findViewById(R.id.task_detail_label_assign_user)).setText(assignUser.getUserName());
    			((TextView)findViewById(R.id.task_detail_label_assign_user)).setTag(assignUser);
    		}
    	}
    }

	private User findUserById(int userId) {
		for(User user : _Users) {
			if(user.getUserId() == userId) {
				return user;
			}
		}
		return null;
	}
}
