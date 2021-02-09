package com.link8.tw.controller.errorCode;

import com.link8.tw.controller.advice.ErrorCode;

public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND;

    @Override
    public String getCode() {
        return this.name().replaceAll("_",".").toLowerCase();
    }
}
