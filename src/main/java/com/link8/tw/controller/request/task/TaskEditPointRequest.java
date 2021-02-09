package com.link8.tw.controller.request.task;

import javax.validation.constraints.Min;

public class TaskEditPointRequest {

    @Min(value = 1)
    private int id;

    private Integer point;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
