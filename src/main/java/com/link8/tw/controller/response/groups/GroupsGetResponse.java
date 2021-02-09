package com.link8.tw.controller.response.groups;

import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.model.Groups;

import java.util.List;
import java.util.stream.Collectors;

public class GroupsGetResponse {

    private int id;

    private String name;

    private BaseUserResponse leader;

    private List<BaseUserResponse> users;

    public GroupsGetResponse() {
    }

    public GroupsGetResponse(Groups groups) {
        this.id = groups.getId();
        this.name = groups.getName();
        this.leader = new BaseUserResponse(groups.getLeader());
        this.users = groups.getUsers().stream().map(BaseUserResponse::new).collect(Collectors.toList());
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

    public List<BaseUserResponse> getUsers() {
        return users;
    }

    public void setUsers(List<BaseUserResponse> users) {
        this.users = users;
    }

    public BaseUserResponse getLeader() {
        return leader;
    }

    public void setLeader(BaseUserResponse leader) {
        this.leader = leader;
    }
}
