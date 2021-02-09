package com.link8.tw.controller.request.task;

import com.link8.tw.model.Task;

public class TaskProcessResponse {

    private int id;

    private String name;

    private String type;

    private String status;

    private long time;

    public TaskProcessResponse() {
    }

    public TaskProcessResponse(Task task, long time) {
        this.id = task.getId();
        this.name = task.getName();
        this.type = task.getType().name();
        this.status = task.getStatus().name();
        this.time = time;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
