package com.fatboy.microtask;

import com.fatboy.microtask.models.Project;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectDetailActivity extends Activity {

	private Project project = null;
	private Boolean InUpdateMode = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_detail);
		
		// Get related Project object.
		Intent intent = this.getIntent();
		project = (Project)intent.getSerializableExtra("tag");
		InUpdateMode = (project != null);
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
		TableRow row_name = (TableRow)findViewById(R.id.tr_project_detail_project_name);
		row_name.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ProjectDetailActivity that = ProjectDetailActivity.this;
				final TextView lbl_name = (TextView)that.findViewById(R.id.project_detail_label_project_name);
				final EditText text_name = new EditText(ProjectDetailActivity.this);
				text_name.setText(lbl_name.getText());
				
				AlertDialog.Builder builder = new Builder(ProjectDetailActivity.this);
				builder.setTitle(getString(R.string.project_detail_dialog_title_project_name));
				builder.setView(text_name);
				builder.setPositiveButton(getString(R.string.project_detail_dialog_ok_button), 
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								lbl_name.setText(text_name.getText());
							}
						});
				builder.setNegativeButton(getString(R.string.project_detail_dialog_cancel_button), null);
				builder.create().show();	
			}
			
		});
		
		TableRow row_desc = (TableRow)findViewById(R.id.tr_project_detail_project_desc);
		row_desc.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ProjectDetailActivity that = ProjectDetailActivity.this;
				final TextView lbl_desc = (TextView)that.findViewById(R.id.project_detail_label_project_desc);
				final EditText text_desc = new EditText(ProjectDetailActivity.this);
				text_desc.setHeight(300);
				text_desc.setGravity(Gravity.LEFT | Gravity.TOP);
				text_desc.setEnabled(true);
				
				Object tag = lbl_desc.getTag();
				if(tag != null) {
					text_desc.setText(Utils.getNeglectString(15, tag.toString()));
				}
				
				AlertDialog.Builder builder = new Builder(ProjectDetailActivity.this);
				builder.setTitle(getString(R.string.project_detail_dialog_title_project_desc));
				builder.setView(text_desc);
				builder.setPositiveButton(getString(R.string.project_detail_dialog_ok_button), 
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String content = text_desc.getText().toString();
								lbl_desc.setTag(content);
								lbl_desc.setText(Utils.getNeglectString(15, content));
							}
						});
				builder.setNegativeButton(getString(R.string.project_detail_dialog_cancel_button), null);
				builder.create().show();	
			}
			
		});
	}

	private void initializeActivity() {
		if(project == null) {
			((TextView)findViewById(R.id.project_detail_label_project_id)).setText("");
			((TextView)findViewById(R.id.project_detail_label_project_name)).setText("");
			((TextView)findViewById(R.id.project_detail_label_project_desc)).setText("");
			((TextView)findViewById(R.id.project_detail_label_project_user)).setText("");
			((TextView)findViewById(R.id.project_detail_label_project_time)).setText("");
		} else {
			((TextView)findViewById(R.id.project_detail_label_project_id)).setText(Integer.toString(project.getProjectId()));
			((TextView)findViewById(R.id.project_detail_label_project_name)).setText(project.getProjectName());
			((TextView)findViewById(R.id.project_detail_label_project_user)).setText(Integer.toString(project.getUserId()));
			((TextView)findViewById(R.id.project_detail_label_project_time)).setText(project.getCreateTimeString());
						
			String content = project.getDescription();
			TextView text_desc = (TextView)findViewById(R.id.project_detail_label_project_desc);
			text_desc.setTag(content);
			text_desc.setText(Utils.getNeglectString(15, content));
		}
	}
	
	private void createProject(final Project prj) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Boolean result = true;

				ProjectVisitor visitor = new ProjectVisitor();
				if (InUpdateMode) {
					result = visitor.modifyProject(prj);
				} else {
					result = (visitor.addProject(prj) > -1);
				}

				if(!result) {
					String errmsg = getString(R.string.project_detail_fail_to_save_project);
					Toast.makeText(ProjectDetailActivity.this, errmsg, Toast.LENGTH_SHORT).show();
				}
				
                Intent intent = new Intent();     
                intent.setClass(ProjectDetailActivity.this, ProjectListActivity.class);
                startActivity(intent);  
                ProjectDetailActivity.this.finish();
			}
		}).start();
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

			TextView text_name = (TextView)findViewById(R.id.project_detail_label_project_name);
			TextView text_desc = (TextView)findViewById(R.id.project_detail_label_project_desc);

			// Create project object.
			Project prj = null;
			if(InUpdateMode) {
				prj = project;
			} else {
				prj = new Project();
				prj.setUserId(1);
			}
			prj.setProjectName(text_name.getText().toString());
			prj.setDescription(text_desc.getText().toString());
		
			createProject(prj);
		}
		
	}
}
