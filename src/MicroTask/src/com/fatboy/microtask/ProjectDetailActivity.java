package com.fatboy.microtask;

import com.fatboy.microtask.models.Project;
import com.fatboy.microtask.visitors.ProjectVisitor;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProjectDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_detail);
		
		// Initialize the action bar.
		ActionBar actionBar = this.getActionBar();
		actionBar.setCustomView(R.layout.detail_titlebar);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.show();
		
//		Button btnCreate = (Button)findViewById(R.id.btnCreateProject);
//		btnCreate.setOnClickListener(new ButtonListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_project, menu);
		return true;
	}
	
	private void createProject(final Project project) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ProjectVisitor visitor = new ProjectVisitor();
				int id = visitor.addProject(project);
				
                Intent intent = new Intent();     
                intent.setClass(ProjectDetailActivity.this, ProjectListActivity.class);
                startActivity(intent);  
                ProjectDetailActivity.this.finish();
			}
		}).start();
	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			EditText txtName = (EditText)findViewById(R.id.txtProjectName);
//			EditText txtDesc = (EditText)findViewById(R.id.txtProjectDesc);
//			
//			// Create project object.
//			Project project = new Project();
//			project.setProjectName(txtName.getText().toString());
//			project.setDescription(txtDesc.getText().toString());
//		
//			createProject(project);
		}
		
	}
}
