package com.link8.tw.security;

import com.link8.tw.controller.response.BaseResponse;
import com.link8.tw.model.User;
import com.tgfc.acl.response.GroupResponse;
import com.tgfc.acl.response.LoginVerifyGeneralResponse;

public class UserLoginResponse {

    private String account;

    private String name;

    private String englishName;

    private BaseResponse group;

    private String img;

    public UserLoginResponse(User user) {
        this.account = user.getAccount();
        this.name = user.getName();
        this.englishName = user.getEnglishName();
        this.group = new BaseResponse(user.getDeptId(),user.getDeptName());
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

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public BaseResponse getGroup() {
        return group;
    }

    public void setGroup(BaseResponse group) {
        this.group = group;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
