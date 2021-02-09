package com.link8.tw.controller.request.task;

import com.link8.tw.enums.Severity;

import javax.validation.constraints.NotEmpty;

public class TaskAddRequest {

    private Integer parent;

    @NotEmpty(message = "任務名稱不能為空")
    private String name;

    private String assign;

    private boolean check = false;

    private Severity severity;

    public TaskAddRequest(Integer parent, String name, String assign, boolean check) {
        this.parent = parent;
        this.name = name;
        this.assign = assign;
        this.check = check;
        this.severity = Severity.GENERAL;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
}
