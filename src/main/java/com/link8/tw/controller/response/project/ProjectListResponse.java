package com.link8.tw.controller.response.project;

import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.model.Project;

public class ProjectListResponse {

    private int id;

    private String name;

    private String status;

    private BaseUserResponse manager;

    private long waitClaimed;

    private long waitToDo;

    private long finish;

    public ProjectListResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.status = project.getStatus().name();
        this.manager = new BaseUserResponse(project.getManager());
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

    public BaseUserResponse getManager() {
        return manager;
    }

    public void setManager(BaseUserResponse manager) {
        this.manager = manager;
    }

    public long getWaitClaimed() {
        return waitClaimed;
    }

    public void setWaitClaimed(long waitClaimed) {
        this.waitClaimed = waitClaimed;
    }

    public long getWaitToDo() {
        return waitToDo;
    }

    public void setWaitToDo(long waitToDo) {
        this.waitToDo = waitToDo;
    }

    public long getFinish() {
        return finish;
    }

    public void setFinish(long finish) {
        this.finish = finish;
    }
}
