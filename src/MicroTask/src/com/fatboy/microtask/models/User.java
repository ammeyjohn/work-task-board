package com.fatboy.microtask.models;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	private int userId;
	
	@SerializedName("name")
	private String userName;
	
	@SerializedName("password")
	private String passsword;
	
	@SerializedName("group_name")
	private String groupName;
	
	@SerializedName("status")
	private int status;
	
	@SerializedName("create_time")
	private Date createTime;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasssword() {
		return passsword;
	}

	public void setPasssword(String passsword) {
		this.passsword = passsword;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
