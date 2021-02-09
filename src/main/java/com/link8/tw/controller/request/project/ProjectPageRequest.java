package com.link8.tw.controller.request.project;

import com.link8.tw.controller.request.PageListRequest;
import com.link8.tw.enums.ProjectStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class ProjectPageRequest extends PageListRequest {

    private String manager;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
}
