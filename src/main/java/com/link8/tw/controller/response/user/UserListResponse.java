package com.link8.tw.controller.response.user;

import com.link8.tw.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserListResponse {

    private String dept;

    private List<BaseUserResponse> users;

    public UserListResponse() {

    }

    public UserListResponse(String deptName, List<User> all) {
        this.dept = deptName;
        this.users = all.stream().filter(e -> e.getDeptName().equals(deptName)).sorted(Comparator.comparing(User::getAccount)).map(BaseUserResponse::new).collect(Collectors.toList());
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public List<BaseUserResponse> getUsers() {
        return users;
    }

    public void setUsers(List<BaseUserResponse> users) {
        this.users = users;
    }
}
