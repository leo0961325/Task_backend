package com.link8.tw.controller.response;

import com.link8.tw.controller.response.groups.GroupsGetResponse;
import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.model.Groups;
import com.link8.tw.model.Project;
import com.link8.tw.model.Task;
import com.link8.tw.repository.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class BaseResponse {

    private Integer id;

    private String name;

    private String manager;

    private List<GroupsGetResponse> groups;

    public BaseResponse() {
    }

    public BaseResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public BaseResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.manager = project.getManager().getAccount();
    }

    public BaseResponse(Groups groups) {
        this.id = groups.getId();
        this.name = groups.getName();
    }

    public BaseResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GroupsGetResponse> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupsGetResponse> groups) {
        this.groups = groups;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
