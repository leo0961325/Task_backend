package com.link8.tw.controller.response.point;

import com.link8.tw.model.Task;
import com.link8.tw.tool.DateTool;

public class PointDetailResponse {

    private int id;

    private String type;

    private String name;

    private String status;

    private Integer point;

    private String finishTime;

    public PointDetailResponse(Task task) {
        this.id = task.getId();
        this.type = task.getType().name();
        this.name = task.getName();
        this.status = task.getStatus().name();
        this.point = task.getPoint();
        this.finishTime = DateTool.getString(task.getFinishTime(),DateTool.yyyy_MM_dd_HH_mm_ss);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
}
