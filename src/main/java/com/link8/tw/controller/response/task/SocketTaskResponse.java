package com.link8.tw.controller.response.task;

import com.link8.tw.enums.SocketType;
import com.link8.tw.enums.TaskColumn;
import com.link8.tw.model.Task;

import java.util.List;

public class SocketTaskResponse {

    private SocketType socketAction;

    private TaskSocketResponse task;

    private List<TaskSocketResponse> taskList;

    public SocketTaskResponse(SocketType socketAction, List<TaskSocketResponse> taskList) {
        this.socketAction = socketAction;
        this.taskList = taskList;
    }

    public SocketTaskResponse(SocketType socketAction, TaskSocketResponse task) {
        this.socketAction = socketAction;
        this.task = task;
    }

    public SocketType getSocketAction() {
        return socketAction;
    }

    public void setSocketAction(SocketType socketAction) {
        this.socketAction = socketAction;
    }

    public TaskSocketResponse getTask() {
        return task;
    }

    public void setTask(TaskSocketResponse task) {
        this.task = task;
    }

    public List<TaskSocketResponse> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskSocketResponse> taskList) {
        this.taskList = taskList;
    }
}
