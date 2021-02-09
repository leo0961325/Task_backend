package com.link8.tw.controller.request.point;

import com.link8.tw.controller.request.PageListRequest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class PointDetailRequest extends PageListRequest {

    @NotEmpty
    private String account;

    @Min(value = 1)
    private Integer projectId;

    @NotEmpty
    private String startTime;

    @NotEmpty
    private String endTime;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getStartTime() {
        return startTime + " 00:00:00";
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime + " 23:59:59";
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
