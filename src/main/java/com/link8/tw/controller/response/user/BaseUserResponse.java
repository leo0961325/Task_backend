package com.link8.tw.controller.response.user;

import com.link8.tw.model.User;
import com.tgfc.acl.response.UserResponse;

public class BaseUserResponse {

    private String account;

    private String name;

    private String img;

    public BaseUserResponse() {
    }

    public BaseUserResponse(User user) {
        this.account = user.getAccount();
        this.name = user.getEnglishName();
        if(user.getImg() != null) {
            this.img = "/footprint-new/api/image/get/" + user.getImg().getId();
        }
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
