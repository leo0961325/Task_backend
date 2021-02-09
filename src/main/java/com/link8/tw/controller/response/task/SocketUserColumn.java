package com.link8.tw.controller.response.task;

import com.link8.tw.enums.TaskColumn;

public class SocketUserColumn {

    private String user;

    private TaskColumn column;

    public SocketUserColumn(String user, TaskColumn column) {
        this.user = user;
        this.column = column;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public TaskColumn getColumn() {
        return column;
    }

    public void setColumn(TaskColumn column) {
        this.column = column;
    }
}
