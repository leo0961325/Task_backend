package com.link8.tw.controller.response.task;

import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.model.Task;
import com.link8.tw.tool.DateTool;

public class TaskProjectFollowResponse {

    private int id;

    private String status;

    private String name;

    private BaseUserResponse dispatcher;

    private BaseUserResponse executor;

    private String startTime;

    private String finishTime;

    private Integer point;

    public TaskProjectFollowResponse(Task task) {
        this.id = task.getId();
        this.status = task.getStatus().name();
        this.name = task.getName();
        if(task.getDispatcher() != null) {
            this.dispatcher = new BaseUserResponse(task.getDispatcher());
        }
        if(task.getAssign() != null) {
            this.executor = new BaseUserResponse(task.getAssign());
        }
        this.startTime = DateTool.getString(task.getStartTime(),DateTool.yyyy_MM_dd_HH_mm_ss);
        this.finishTime = DateTool.getString(task.getFinishTime(),DateTool.yyyy_MM_dd_HH_mm_ss);
        this.point = task.getPoint();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseUserResponse getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(BaseUserResponse dispatcher) {
        this.dispatcher = dispatcher;
    }

    public BaseUserResponse getExecutor() {
        return executor;
    }

    public void setExecutor(BaseUserResponse executor) {
        this.executor = executor;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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
}
