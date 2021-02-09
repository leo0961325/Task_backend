package com.link8.tw.controller.errorCode;

import com.link8.tw.controller.advice.ErrorCode;

public enum TransErrorCode implements ErrorCode {

    TRANS_DATE_FAIL_WEEK,
    TRANS_DATE_FAIL_MONTH,
    TRANS_DATE_FAIL,
    TRANS_JSON_FAIL;

    TransErrorCode() {
    }

    @Override
    public String getCode() {
        return this.name().replaceAll("_",".").toLowerCase();
    }
}
