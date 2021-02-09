package com.link8.tw.controller.response.project;

import com.link8.tw.model.Project;

public class ProjectSubscribeResponse {

    private int id;

    private String name;

    private String status;

    private boolean subscribe;

    public ProjectSubscribeResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.status = project.getStatus().name();
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

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}
