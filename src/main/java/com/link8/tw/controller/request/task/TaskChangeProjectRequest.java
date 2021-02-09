package com.link8.tw.controller.request.task;

import java.util.List;

public class TaskChangeProjectRequest {

    private List<Integer> id;

    private Integer projectId;

    public TaskChangeProjectRequest(List<Integer> id, int projectId) {
        this.id = id;
        this.projectId = projectId;
    }

    public List<Integer> getId() {
        return id;
    }

    public void setId(List<Integer> id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
