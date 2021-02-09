package com.link8.tw.controller.request.message;

public class MessageAddRequest {

    private int taskId;

    private String context;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
