package com.link8.tw.controller.response.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.model.History;
import com.link8.tw.tool.DateTool;

import java.util.List;

public class TaskHistoryResponse {

    private int id;

    private BaseUserResponse executor;

    private String action;

    private List<TaskEditResponse> changeItem;

    private String time;

    private String context;

    public TaskHistoryResponse(History history) {
        this.id = history.getId();
        this.executor = new BaseUserResponse(history.getExecutor());
        this.action = history.getAction().name();
        if (history.getChangeItem() != null) {
            this.changeItem = new Gson().fromJson(history.getChangeItem(), new TypeToken<List<TaskEditResponse>>() {
            }.getType());
        }
        this.time = DateTool.getString(history.getTime(), DateTool.yyyy_MM_dd_HH_mm_ss);
        this.context = history.getContext();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BaseUserResponse getExecutor() {
        return executor;
    }

    public void setExecutor(BaseUserResponse executor) {
        this.executor = executor;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<TaskEditResponse> getChangeItem() {
        return changeItem;
    }

    public void setChangeItem(List<TaskEditResponse> changeItem) {
        this.changeItem = changeItem;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
