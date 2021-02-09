package com.link8.tw.controller.response.user;

import com.link8.tw.model.User;

public class UserResponse {

    private String account;

    private String name;

    private String dept;

    private String img;

    public UserResponse(User user) {
        this.account = user.getAccount();
        this.name = user.getEnglishName();
        this.dept = user.getDeptName();
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

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
