package com.link8.tw.controller.response.point;

import com.link8.tw.controller.response.BaseResponse;
import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.model.Project;
import com.link8.tw.model.Task;
import com.link8.tw.model.User;

import java.util.List;

public class TaskStatisticResponse {

    private BaseUserResponse user;

    private BaseResponse project;

    private Integer taskCount;

    private int score;

    public TaskStatisticResponse(List<Task> task, User user, Project project) {
        this.user = new BaseUserResponse(user);
        this.project = new BaseResponse(project);
        this.taskCount = task.size();
        this.score = task.stream().filter(e -> e.getPoint() != null).mapToInt(Task::getPoint).sum();
    }

    public BaseUserResponse getUser() {
        return user;
    }

    public void setUser(BaseUserResponse user) {
        this.user = user;
    }

    public BaseResponse getProject() {
        return project;
    }

    public void setProject(BaseResponse project) {
        this.project = project;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
