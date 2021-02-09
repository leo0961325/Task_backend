package com.link8.tw.controller.response.message;

import com.link8.tw.controller.response.user.UserResponse;
import com.link8.tw.model.Message;
import com.link8.tw.model.User;
import com.link8.tw.tool.DateTool;

public class MessageResponse {

    private int id;

    private String context;

    private UserResponse sender;

    private String time;

    private boolean read;

    public MessageResponse(Message message, User login) {
        this.id = message.getId();
        this.context = message.getContext();
        this.sender = new UserResponse(message.getUser());
        this.time = DateTool.getString(message.getTime(),DateTool.yyyy_MM_dd_HH_mm_ss);
        this.read = message.getReadUser().contains(login.getAccount());
    }

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.context = message.getContext();
        this.sender = new UserResponse(message.getUser());
        this.time = DateTool.getString(message.getTime(),DateTool.yyyy_MM_dd_HH_mm_ss);
        this.read = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public UserResponse getSender() {
        return sender;
    }

    public void setSender(UserResponse sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
