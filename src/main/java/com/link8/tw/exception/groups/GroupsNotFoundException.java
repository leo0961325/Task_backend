package com.link8.tw.exception.groups;

import com.link8.tw.controller.advice.ErrorCode;
import com.link8.tw.controller.advice.ErrorCodeException;

public class GroupsNotFoundException extends ErrorCodeException {

    public GroupsNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
