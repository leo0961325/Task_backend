package com.link8.tw.exception.trans;

import com.link8.tw.controller.advice.ErrorCode;

public class TransJsonException extends Exception{

    private ErrorCode errorCode;

    public TransJsonException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
