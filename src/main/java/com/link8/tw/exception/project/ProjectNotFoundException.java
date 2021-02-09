package com.link8.tw.exception.project;

import com.link8.tw.controller.advice.ErrorCode;
import com.link8.tw.controller.advice.ErrorCodeException;

public class ProjectNotFoundException extends ErrorCodeException {

    public ProjectNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
