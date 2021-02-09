package com.link8.tw.exception.task;

import com.link8.tw.controller.advice.ErrorCode;

public class TaskExistException extends Exception {

    private ErrorCode errorCode;

    public TaskExistException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
