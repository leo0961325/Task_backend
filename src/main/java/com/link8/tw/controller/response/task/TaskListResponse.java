package com.link8.tw.controller.response.task;

import com.link8.tw.controller.response.BaseResponse;
import com.link8.tw.controller.response.user.UserResponse;
import com.link8.tw.model.Task;

public class TaskListResponse {

    private int id;

    private String type;

    private String status;

    private String name;

    private UserResponse executor;

    private BaseResponse project;

    private Integer point;

    private UserResponse creator;

    public TaskListResponse(Task task) {
        this.id = task.getId();
        this.type = task.getType().name();
        this.status = task.getStatus().name();
        this.name = task.getName();
        if(task.getAssign() != null) {
            this.executor = new UserResponse(task.getAssign());
        }
        if(task.getProject() != null) {
            this.project = new BaseResponse(task.getProject());
        }
        this.point = task.getPoint();
        this.creator = new UserResponse(task.getCreator());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserResponse getExecutor() {
        return executor;
    }

    public void setExecutor(UserResponse executor) {
        this.executor = executor;
    }

    public BaseResponse getProject() {
        return project;
    }

    public void setProject(BaseResponse project) {
        this.project = project;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public UserResponse getCreator() {
        return creator;
    }

    public void setCreator(UserResponse creator) {
        this.creator = creator;
    }
}
