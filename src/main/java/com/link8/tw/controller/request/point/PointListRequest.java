package com.link8.tw.controller.request.point;

import com.link8.tw.controller.request.PageListRequest;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PointListRequest extends PageListRequest {

    private Integer projectId;

    private List<String> accountList;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    private String sort = "project";

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public List<String> getAccountList() {
        if (this.accountList == null) {
            this.accountList = new ArrayList<>();
        }
        return accountList;
    }

    public void setAccountList(List<String> accountList) {
        this.accountList = accountList;
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
