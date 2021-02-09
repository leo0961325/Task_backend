package com.link8.tw.controller.errorCode;

import com.link8.tw.controller.advice.ErrorCode;

public enum TaskErrorCode implements ErrorCode {

    TASK_NOT_FOUND;


    @Override
    public String getCode() {
        return this.name().replaceAll("_",".").toLowerCase();
    }
}
