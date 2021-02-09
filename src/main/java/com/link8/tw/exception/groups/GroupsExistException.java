package com.link8.tw.exception.groups;

import com.link8.tw.controller.advice.ErrorCode;
import com.link8.tw.controller.advice.ErrorCodeException;

public class GroupsExistException extends ErrorCodeException {

    public GroupsExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
