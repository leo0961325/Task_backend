package com.link8.tw.controller.response.task;

import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.model.Task;

public class TaskSimpleResponse {

    private int id;

    private String name;

    private String status;

    private BaseUserResponse assign;

    public TaskSimpleResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.status = task.getStatus().name();
        if (task.getAssign() != null) {
            this.assign = new BaseUserResponse(task.getAssign());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BaseUserResponse getAssign() {
        return assign;
    }

    public void setAssign(BaseUserResponse assign) {
        this.assign = assign;
    }
}
