package com.link8.tw.controller.request.project;

import com.link8.tw.controller.request.groups.GroupsAddRequest;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class ProjectAddRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String manager;

    private List<GroupsAddRequest> groups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public List<GroupsAddRequest> getGroups() {
        if(this.groups == null) {
            this.groups = new ArrayList<>();
        }
        return groups;
    }

    public void setGroups(List<GroupsAddRequest> groups) {
        this.groups = groups;
    }
}
