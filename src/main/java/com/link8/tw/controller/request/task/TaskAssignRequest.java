package com.link8.tw.controller.request.task;

import java.util.List;

public class TaskAssignRequest {

    private List<Integer> id;

    private String assign;

    private boolean follow;


    public TaskAssignRequest(List<Integer> id, String assign, boolean follow) {
        this.id = id;
        this.assign = assign;
        this.follow = follow;
    }

    public void setId(List<Integer> id) {
        this.id = id;
    }

    public List<Integer> getId() {
        return id;
    }

    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }
}
