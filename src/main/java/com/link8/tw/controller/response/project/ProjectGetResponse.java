package com.link8.tw.controller.response.project;

import com.link8.tw.controller.response.groups.GroupsGetResponse;
import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.model.Project;

import java.util.List;

public class ProjectGetResponse {

    private int id;

    private String name;

    private String status;

    private BaseUserResponse manager;

    private List<GroupsGetResponse> groups;

    public ProjectGetResponse(Project project) {
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

    public List<GroupsGetResponse> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupsGetResponse> groups) {
        this.groups = groups;
    }
}
