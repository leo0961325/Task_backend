package com.link8.tw.controller.request.task;

import javax.validation.constraints.Min;

public class TaskCheckRequest {

    @Min(value = 1)
    private int id;

    private String account;

    public TaskCheckRequest(int id, String account) {
        this.id = id;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
