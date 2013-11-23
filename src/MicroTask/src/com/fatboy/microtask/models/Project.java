package com.fatboy.microtask.models;

import java.io.Serializable;
import java.util.Date;

import com.fatboy.microtask.utils.Utils;
import com.google.gson.annotations.SerializedName;

public class Project 
	implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@SerializedName("id")
    private int projectId;

    @SerializedName("name")
    private String projectName;

    @SerializedName("status")
    private int status;
    
    @SerializedName("desc")
    private String description;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("create_time")
    private Date createTime;

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

	public Date getCreateTime() {
		return createTime;
	}
	
	public String getCreateTimeString() {
		return Utils.getDateString(createTime);
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
