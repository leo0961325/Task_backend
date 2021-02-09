package com.link8.tw.controller.response.task;

import com.link8.tw.controller.response.BaseResponse;
import com.link8.tw.controller.response.groups.GroupsGetResponse;
import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.controller.response.user.UserResponse;
import com.link8.tw.enums.Severity;
import com.link8.tw.model.Task;
import com.link8.tw.tool.DateTool;

import java.util.List;

public class TaskSocketResponse {

    private int id;

    private String name;

    private UserResponse assign;

    private UserResponse creator;

    private UserResponse dispatcher;

    private BaseResponse project;

    private String status;

    private String type;

    private String finishTime;

    private Integer point;

    private List<TaskSimpleResponse> sub;

    private int notRead;

    private boolean hasMessage;

    private boolean executorIsRead;

    private List<BaseUserResponse> follow;

    private Severity severity;

    private List<GroupsGetResponse> groups;

    public TaskSocketResponse() {
    }

    public TaskSocketResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        if (task.getAssign() != null) {
            this.assign = new UserResponse(task.getAssign());
        }
        if (task.getProject() != null) {
            this.project = new BaseResponse(task.getProject());
        }
        this.point = task.getPoint();
        this.creator = new UserResponse(task.getCreator());
        this.status = task.getStatus().name();
        this.type = task.getType().name();
        if(task.getFinishTime() != null) {
            this.finishTime = DateTool.getString(task.getFinishTime(),DateTool.yyyy_MM_dd);
        }
        if(task.getDispatcher() != null) {
            this.dispatcher = new UserResponse(task.getDispatcher());
        }
        this.executorIsRead = task.isRead();
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

    public UserResponse getAssign() {
        return assign;
    }

    public void setAssign(UserResponse assign) {
        this.assign = assign;
    }

    public UserResponse getCreator() {
        return creator;
    }

    public void setCreator(UserResponse creator) {
        this.creator = creator;
    }

    public BaseResponse getProject() {
        return project;
    }

    public void setProject(BaseResponse project) {
        this.project = project;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public List<TaskSimpleResponse> getSub() {
        return sub;
    }

    public void setSub(List<TaskSimpleResponse> sub) {
        this.sub = sub;
    }

    public UserResponse getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(UserResponse dispatcher) {
        this.dispatcher = dispatcher;
    }

    public int getNotRead() {
        return notRead;
    }

    public void setNotRead(int notRead) {
        this.notRead = notRead;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }

    public void setHasMessage(boolean hasMessage) {
        this.hasMessage = hasMessage;
    }

    public boolean isExecutorIsRead() {
        return executorIsRead;
    }

    public void setExecutorIsRead(boolean executorIsRead) {
        this.executorIsRead = executorIsRead;
    }

    public List<BaseUserResponse> getFollow() {
        return follow;
    }

    public void setFollow(List<BaseUserResponse> follow) {
        this.follow = follow;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public List<GroupsGetResponse> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupsGetResponse> groups) {
        this.groups = groups;
    }
}
