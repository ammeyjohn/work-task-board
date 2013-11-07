package com.example.worktaskboard;

import com.google.gson.annotations.SerializedName;

/**
 * Created by patrick on 11/6/13.
 */
public class Task {

    @SerializedName("id")
    private int taskId;

    @SerializedName("content")
    private String taskContent;

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
}
