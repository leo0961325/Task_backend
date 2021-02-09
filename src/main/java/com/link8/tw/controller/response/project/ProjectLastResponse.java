package com.link8.tw.controller.response.project;

import com.link8.tw.model.Project;

public class ProjectLastResponse {

    private int id;

    private String name;

    private boolean last;

    public ProjectLastResponse(Project project, boolean last) {
        this.id = project.getId();
        this.name = project.getName();
        this.last = last;
    }

    public ProjectLastResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.last = false;
    }

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

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
