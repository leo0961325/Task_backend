package com.link8.tw.exception;

import com.link8.tw.controller.advice.ErrorCode;
import com.link8.tw.controller.advice.ErrorCodeException;

public class PermissionDeniedException extends ErrorCodeException {

    public PermissionDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
