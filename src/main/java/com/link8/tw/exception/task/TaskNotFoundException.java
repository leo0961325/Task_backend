package com.link8.tw.exception.task;

import com.link8.tw.controller.advice.ErrorCode;
import com.link8.tw.controller.advice.ErrorCodeException;

public class TaskNotFoundException extends ErrorCodeException {

    public TaskNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
