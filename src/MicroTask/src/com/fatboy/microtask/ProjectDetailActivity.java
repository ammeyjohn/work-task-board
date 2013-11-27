package com.fatboy.microtask;

import java.util.Date;

import com.fatboy.microtask.models.Global;
import com.fatboy.microtask.models.Project;
import com.fatboy.microtask.models.User;
import com.fatboy.microtask.utils.Utils;
import com.fatboy.microtask.visitors.ProjectVisitor;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectDetailActivity extends Activity {

	final ProjectDetailActivity That = this;
	
	private Project _Project = null;
	private Boolean _InUpdateMode = true;
	
	private TextView mIdView;
	private TextView mNameView;
	private TextView mDescView;
	private TextView mUserView;
	private TextView mTimeView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_detail);
		
		// Retrieves components.
		mIdView = (TextView)findViewById(R.id.project_detail_label_project_id);
		mNameView = (TextView)findViewById(R.id.project_detail_label_project_name);
		mDescView = (TextView)findViewById(R.id.project_detail_label_project_desc);
		mUserView = (TextView)findViewById(R.id.project_detail_label_project_user);
		mTimeView = (TextView)findViewById(R.id.project_detail_label_project_time);
		
		// Get related Project object.
		Intent intent = this.getIntent();
		_Project = (Project)intent.getSerializableExtra("tag");
		_InUpdateMode = (_Project != null);
		initializeActivity();
		
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
		
		// Binding click event to table row.
		TableRow rowName = (TableRow)findViewById(R.id.tr_project_detail_project_name);
		rowName.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				final EditText txtName = new EditText(That);
				txtName.setText(mNameView.getText());
				
				AlertDialog.Builder builder = new Builder(That);
				builder.setTitle(getString(R.string.project_detail_dialog_title_project_name));
				builder.setView(txtName);
				builder.setPositiveButton(getString(R.string.project_detail_dialog_ok_button), 
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mNameView.setText(txtName.getText());
							}
						});
				builder.setNegativeButton(getString(R.string.project_detail_dialog_cancel_button), null);
				builder.create().show();	
			}
			
		});
		
		TableRow rowDesc = (TableRow)findViewById(R.id.tr_project_detail_project_desc);
		rowDesc.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				final EditText txtDesc = new EditText(ProjectDetailActivity.this);
				txtDesc.setHeight(300);
				txtDesc.setGravity(Gravity.LEFT | Gravity.TOP);
				txtDesc.setEnabled(true);
				
				Object tag = mDescView.getTag();
				if(tag != null) {
					txtDesc.setText(tag.toString());
				}
				
				AlertDialog.Builder builder = new Builder(That);
				builder.setTitle(getString(R.string.project_detail_dialog_title_project_desc));
				builder.setView(txtDesc);
				builder.setPositiveButton(getString(R.string.project_detail_dialog_ok_button), 
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String content = txtDesc.getText().toString();
								mDescView.setText(Utils.getNeglectString(25, content));
								mDescView.setTag(content);
							}
						});
				builder.setNegativeButton(getString(R.string.project_detail_dialog_cancel_button), null);
				builder.create().show();	
			}
			
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	doBack();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}

	private void initializeActivity() {
		
		Global g = (Global)getApplicationContext();
		
		if(_Project == null) {
			mIdView.setText("");
			mNameView.setText("");
			mDescView.setText("");
			mDescView.setTag("");
			
			User user = g.getCurrentUser();
			mUserView.setText(user.getUserName());
			mUserView.setTag(user);

			Date now = new Date();	
			mTimeView.setText(Utils.getDateTimeString(now));
			mTimeView.setTag(now);
		} else {
			mIdView.setText(Integer.toString(_Project.getProjectId()));
			mNameView.setText(_Project.getProjectName());
			mDescView.setText(Utils.getNeglectString(25, _Project.getDescription()));
			mDescView.setTag(_Project.getDescription());
			
			User user = Utils.findUserById(g.getUsers(), _Project.getUserId());
			mUserView.setText(user.getUserName());
			mUserView.setTag(user);

			Date now = _Project.getCreateTime();
			mTimeView.setText(Utils.getDateTimeString(now));
			mTimeView.setTag(now);
		}
	}
	
	private void createProject(final Project prj) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Boolean result = true;

				ProjectVisitor visitor = new ProjectVisitor();
				if (_InUpdateMode) {
					result = visitor.modifyProject(prj);
				} else {
					result = (visitor.addProject(prj) > -1);
				}

				if(!result) {
					String errmsg = getString(R.string.project_detail_fail_to_save_project);
					Toast.makeText(ProjectDetailActivity.this, errmsg, Toast.LENGTH_SHORT).show();
				}
		
				// Back to previous form.
				doBack();
			}
		}).start();
	}
	
	private void doBack(){
        Intent intent = new Intent();     
        intent.setClass(ProjectDetailActivity.this, ProjectListActivity.class);
        startActivity(intent);  
        finish();
	}

	class ButtonBackListener implements OnClickListener {

		@Override
		public void onClick(View v) {
            Intent intent = new Intent();     
            intent.setClass(ProjectDetailActivity.this, ProjectListActivity.class);
            startActivity(intent);  
            ProjectDetailActivity.this.finish();
		}
		
	}

	class ButtonSaveListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// Create project object.
			if(_Project == null) {
				Global g = (Global)That.getApplicationContext();
				_Project = new Project();
				_Project.setUserId(g.getCurrentUser().getUserId());
			}
			_Project.setProjectName(mNameView.getText().toString());
			_Project.setDescription(mDescView.getTag().toString());
		
			createProject(_Project);
		}
		
	}
}
