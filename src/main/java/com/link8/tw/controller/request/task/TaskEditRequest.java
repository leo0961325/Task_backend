package com.link8.tw.controller.request.task;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

public class TaskEditRequest {

    @Min(value = 1)
    private int id;

    @NotEmpty
    private String name;

    private String context;

    private String assign;

    private Integer projectId;

    private TaskScheduleRequest schedule;

    private Integer estimateTime;

    private LocalDate estimateStartTime;

    private LocalDate estimateEndTime;

    private Integer point;

    private String checkMan;

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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public TaskScheduleRequest getSchedule() {
        return schedule;
    }

    public void setSchedule(TaskScheduleRequest schedule) {
        this.schedule = schedule;
    }

    public Integer getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(Integer estimateTime) {
        this.estimateTime = estimateTime;
    }

    public LocalDate getEstimateStartTime() {
        return estimateStartTime;
    }

    public void setEstimateStartTime(LocalDate estimateStartTime) {
        this.estimateStartTime = estimateStartTime;
    }

    public LocalDate getEstimateEndTime() {
        return estimateEndTime;
    }

    public void setEstimateEndTime(LocalDate estimateEndTime) {
        this.estimateEndTime = estimateEndTime;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getCheckMan() {
        return checkMan;
    }

    public void setCheckMan(String checkMan) {
        this.checkMan = checkMan;
    }
}
