package com.link8.tw.controller.request.point;

import java.time.LocalDateTime;

public class PointRequest {

    private String account;
    private LocalDateTime localDateTime;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
