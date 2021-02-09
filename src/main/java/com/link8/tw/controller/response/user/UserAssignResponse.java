package com.link8.tw.controller.response.user;

import java.util.List;

public class UserAssignResponse {

    private List<UserResponse> recent;

    private List<UserListResponse> user;

    public List<UserResponse> getRecent() {
        return recent;
    }

    public void setRecent(List<UserResponse> recent) {
        this.recent = recent;
    }

    public List<UserListResponse> getUser() {
        return user;
    }

    public void setUser(List<UserListResponse> user) {
        this.user = user;
    }
}
