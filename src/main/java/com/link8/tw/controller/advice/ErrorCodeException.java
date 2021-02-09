package com.link8.tw.controller.advice;

public class ErrorCodeException extends Exception {

    private ErrorCode errorCode;

    public ErrorCodeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
