package com.link8.tw.exception;

import com.link8.tw.controller.advice.ErrorCode;
import com.link8.tw.controller.advice.ErrorCodeException;

public class ActionFailException extends ErrorCodeException {

    public ActionFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
