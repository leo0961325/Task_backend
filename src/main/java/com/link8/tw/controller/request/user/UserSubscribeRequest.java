package com.link8.tw.controller.request.user;

public class UserSubscribeRequest {

    private String account;

    private boolean subscribe;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}
