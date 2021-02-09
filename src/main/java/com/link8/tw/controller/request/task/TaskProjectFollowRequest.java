package com.link8.tw.controller.request.task;

import com.link8.tw.enums.Status;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class TaskProjectFollowRequest {

    private Integer projectId;

    private Integer groupId;

    private String keyword;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String sortKey = "id";

    private String order = "ascend";

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
