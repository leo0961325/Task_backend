package com.link8.tw.controller.request.project;

import com.link8.tw.controller.request.groups.GroupsAddRequest;
import com.link8.tw.controller.request.groups.GroupsEditRequest;
import com.link8.tw.enums.ProjectStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ProjectEditRequest {

    @Min(value = 1)
    private int id;

    @NotEmpty
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @NotEmpty
    private String manager;

    private List<GroupsAddRequest> addGroups;

    private List<GroupsEditRequest> editGroups;

    private List<Integer> removeGroups;

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

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public List<GroupsAddRequest> getAddGroups() {
        if(this.addGroups == null) {
            this.addGroups = new ArrayList<>();
        }
        return addGroups;
    }

    public void setAddGroups(List<GroupsAddRequest> addGroups) {
        this.addGroups = addGroups;
    }

    public List<GroupsEditRequest> getEditGroups() {
        if(this.editGroups == null) {
            this.editGroups = new ArrayList<>();
        }
        return editGroups;
    }

    public void setEditGroups(List<GroupsEditRequest> editGroups) {
        this.editGroups = editGroups;
    }

    public List<Integer> getRemoveGroups() {
        if(this.removeGroups == null) {
            this.removeGroups = new ArrayList<>();
        }
        return removeGroups;
    }

    public void setRemoveGroups(List<Integer> removeGroups) {
        this.removeGroups = removeGroups;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
}
