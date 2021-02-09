package com.link8.tw.exception.trans;

import com.link8.tw.controller.advice.ErrorCode;
import com.link8.tw.controller.advice.ErrorCodeException;

public class TransDateException extends ErrorCodeException {

    public TransDateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
