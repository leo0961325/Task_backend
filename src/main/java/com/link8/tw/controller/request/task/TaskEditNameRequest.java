package com.link8.tw.controller.request.task;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class TaskEditNameRequest {

    @Min(value = 1)
    private int id;

    @NotEmpty
    private String name;

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
}
