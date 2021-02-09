package com.link8.tw.controller.response.project;

import com.link8.tw.controller.response.BaseResponse;
import com.link8.tw.enums.Status;
import com.link8.tw.model.Groups;
import com.link8.tw.model.Task;
import com.link8.tw.model.User;

import java.util.List;

public class ProjectFollowResponse {

    private BaseResponse project;

    private BaseResponse group;

    private int taskNnm;

    public ProjectFollowResponse(Groups group, User user, List<Task> tasks) {
        this.project = new BaseResponse(group.getProject());
        this.group = new BaseResponse(group);
        if(tasks != null) {
            this.taskNnm = (int) tasks.stream().filter(e -> (e.getStatus() == Status.PAUSE
                    || e.getStatus() == Status.START_NOT_YET)
                    && e.getAssign().getAccount().equals(user.getAccount())
                    && e.getProject().getId() == group.getProject().getId()).count();
        }
    }

    public BaseResponse getProject() {
        return project;
    }

    public void setProject(BaseResponse project) {
        this.project = project;
    }

    public BaseResponse getGroup() {
        return group;
    }

    public void setGroup(BaseResponse group) {
        this.group = group;
    }

    public int getTaskNnm() {
        return taskNnm;
    }

    public void setTaskNnm(int taskNnm) {
        this.taskNnm = taskNnm;
    }
}
