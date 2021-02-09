package com.link8.tw.controller.response.project;

import com.link8.tw.model.Project;

public class ProjectResponse {

    private Integer projectId;
    private String name;
    private String dept;
    private Integer taskNum;

    public ProjectResponse() {
    }

    public ProjectResponse(Project project) {
        this.projectId = project.getId();
        this.name = project.getName();
        this.dept = project.getDept();
    }

    public ProjectResponse(Project project, Integer num) {
        this.projectId = project.getId();
        this.name = project.getName();
        this.dept = project.getDept();
        this.taskNum = num;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }
}
