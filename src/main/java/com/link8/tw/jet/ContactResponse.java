package com.link8.tw.jet;

import java.util.List;

public class ContactResponse {
    private int code;
    private String msg;
    private List<JetUser> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<JetUser> getData() {
        return data;
    }

    public void setData(List<JetUser> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
