package com.fatboy.microtask.models;

import java.io.Serializable;
import java.util.Date;

import com.fatboy.microtask.utils.Utils;
import com.google.gson.annotations.SerializedName;

public class Task implements Serializable {
	
	private static final long serialVersionUID = 1L;

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
    private Date createTime;

    @SerializedName("update_time")
    private Date updateTime;

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
	
	public String getStatusString() {
		switch(status) {
		case -1: return "已删除";
		case 1: return "已创建";
		case 2: return "已分配";
		case 3: return "已完成";
		case 4: return "已确认";
		}
		return Integer.toString(status);
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateTimeString() {
		return Utils.getDateString(createTime);
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}	   
	
	public String getUpdateTimeString() {
		return Utils.getDateString(updateTime);
	}
}
