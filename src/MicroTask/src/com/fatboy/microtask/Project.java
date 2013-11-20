package com.fatboy.microtask;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Project implements Serializable {
	
    @SerializedName("id")
    private int projectId;

    @SerializedName("name")
    private String projectName;

    @SerializedName("status")
    private int status;
    
    @SerializedName("description")
    private String description;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("create_time")
    private String createTime;

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
