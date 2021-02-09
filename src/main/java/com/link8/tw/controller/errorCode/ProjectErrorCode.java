package com.link8.tw.controller.errorCode;

import com.link8.tw.controller.advice.ErrorCode;

public enum ProjectErrorCode implements ErrorCode {

    PROJECT_NOT_FOUND,
    PROJECT_EXIST,
    PROJECT_HAS_TASK,
    GROUPS_NOT_FOUND,
    GROUPS_EXIST,
    GROUP_LEADER_HAS_IN;

    ProjectErrorCode() {
    }

    @Override
    public String getCode() {
        return this.name().replaceAll("_",".").toLowerCase();
    }
}
