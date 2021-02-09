package com.link8.tw.controller.request;

import java.util.ArrayList;
import java.util.List;

public class TaskPageRequest extends PageListRequest {

    private List<Integer> projectId;

    private List<String> account;

    public List<Integer> getProjectId() {
        if(this.projectId == null) {
            this.projectId = new ArrayList<>();
        }
        return projectId;
    }

    public void setProjectId(List<Integer> projectId) {
        this.projectId = projectId;
    }

    public List<String> getAccount() {
        if(this.account == null) {
            this.account = new ArrayList<>();
        }
        return account;
    }

    public void setAccount(List<String> account) {
        this.account = account;
    }
}
