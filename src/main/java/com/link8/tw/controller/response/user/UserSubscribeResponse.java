package com.link8.tw.controller.response.user;

import com.link8.tw.model.User;

public class UserSubscribeResponse extends UserResponse{

    private boolean subscribe = false;

    public UserSubscribeResponse(User user) {
        super(user);
    }

    public UserSubscribeResponse(User user, User login) {
        super(user);
        if(login.getSubscribe().stream().anyMatch(e -> e.getAccount().equals(user.getAccount()))) {
            this.subscribe = true;
        }
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}
