package com.link8.tw.exception.user;

import com.link8.tw.controller.advice.ErrorCode;
import com.link8.tw.controller.advice.ErrorCodeException;

public class UserNotFoundException extends ErrorCodeException {

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
