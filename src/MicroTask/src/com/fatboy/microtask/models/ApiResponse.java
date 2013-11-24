package com.fatboy.microtask.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T>
	implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("result")
	private Boolean result;
	
	@SerializedName("action")
	private String action;
	
	@SerializedName("obj")
	private T data;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
