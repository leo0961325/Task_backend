package com.link8.tw.controller.request.user;

public class ProjectSubscribeRequest {

    private int projectId;

    private boolean subscribe;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}
