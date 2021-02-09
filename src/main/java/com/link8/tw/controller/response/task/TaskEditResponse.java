package com.link8.tw.controller.response.task;

public class TaskEditResponse {

    private String column;

    private String before;

    private String after;

    public TaskEditResponse(String column, String before, String after) {
        this.column = column;
        this.before = before;
        this.after = after;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
