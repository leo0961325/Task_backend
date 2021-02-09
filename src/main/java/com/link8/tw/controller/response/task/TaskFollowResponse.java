package com.link8.tw.controller.response.task;

import com.link8.tw.controller.response.BaseResponse;
import com.link8.tw.controller.response.project.ProjectFollowResponse;
import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.controller.response.user.UserResponse;
import com.link8.tw.model.Task;
import com.link8.tw.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskFollowResponse {

    private UserResponse user;

    private boolean login;

    private BaseResponse process;

    private List<ProjectFollowResponse> projects;

    public TaskFollowResponse(User user, Optional<Task> process) {
        this.user = new UserResponse(user);
        if (process.isPresent()) {
            this.process = new BaseResponse(process.get());
        }
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public BaseResponse getProcess() {
        return process;
    }

    public void setProcess(BaseResponse process) {
        this.process = process;
    }

    public List<ProjectFollowResponse> getProjects() {
        if (this.projects == null) {
            this.projects = new ArrayList<>();
        }
        return projects;
    }

    public void setProjects(List<ProjectFollowResponse> projects) {
        this.projects = projects;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
