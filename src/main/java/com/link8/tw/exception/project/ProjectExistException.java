package com.link8.tw.exception.project;

import com.link8.tw.controller.advice.ErrorCode;
import com.link8.tw.controller.advice.ErrorCodeException;

public class ProjectExistException extends ErrorCodeException {

    public ProjectExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
