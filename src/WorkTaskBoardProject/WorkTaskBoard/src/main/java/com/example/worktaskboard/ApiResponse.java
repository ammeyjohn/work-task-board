package com.example.worktaskboard;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by patrick on 11/9/13.
 */
public class ApiResponse<T> implements Serializable {

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