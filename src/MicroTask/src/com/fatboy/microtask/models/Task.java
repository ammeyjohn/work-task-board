package com.fatboy.microtask.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Task implements Serializable {
	
    @SerializedName("id")
    private int taskId;

    @SerializedName("content")
    private String taskContent;

    @SerializedName("type")
    private int taskType;

    @SerializedName("status")
    private int status;

    @SerializedName("project_id")
    private int projectId;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("create_time")
    private String createTime;

    @SerializedName("update_time")
    private String updateTime;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getTaskContent() {
		return taskContent;
	}

	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}	   
}
