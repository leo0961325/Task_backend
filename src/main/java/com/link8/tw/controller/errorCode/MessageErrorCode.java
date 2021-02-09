package com.link8.tw.controller.errorCode;

import com.link8.tw.controller.advice.ErrorCode;

public enum MessageErrorCode implements ErrorCode {
    MESSAGE_NOT_FOUND;

    MessageErrorCode() {
    }


    @Override
    public String getCode() {
        return this.name().replaceAll("_",".").toLowerCase();
    }
}
