package com.link8.tw.controller.request.task;

import java.util.ArrayList;
import java.util.List;

public class TaskUserFollowRequest {

    private List<String> accounts;
    private List<Integer> projectId;
    private Integer status;

    public List<String> getAccounts() {
        if(this.accounts == null) {
            this.accounts = new ArrayList<>();
        }
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public List<Integer> getProjectId() {
        if(this.projectId == null) {
            this.projectId = new ArrayList<>();
        }
        return projectId;
    }

    public void setProjectId(List<Integer> projectId) {
        this.projectId = projectId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
