package com.link8.tw.controller.response.task;

import com.link8.tw.controller.response.BaseResponse;
import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.enums.Status;
import com.link8.tw.model.Task;

public class TaskCloseResponse {

    private int id;

    private String name;

    private String type;

    private BaseResponse project;

    private String board;

    private BaseUserResponse executor;

    private Status status;

    public TaskCloseResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.type = task.getType().name();
        if (task.getProject() != null) {
            this.project = new BaseResponse(task.getProject());
        }
        if (task.getAssign() == null) {
            this.board = "待認領";
        } else {
            this.board = "待處理";
            this.executor = new BaseUserResponse(task.getAssign());
        }
        this.status = task.getStatus();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BaseResponse getProject() {
        return project;
    }

    public void setProject(BaseResponse project) {
        this.project = project;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public BaseUserResponse getExecutor() {
        return executor;
    }

    public void setExecutor(BaseUserResponse executor) {
        this.executor = executor;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
