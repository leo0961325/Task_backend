package com.link8.tw.controller.errorCode;

import com.link8.tw.controller.advice.ErrorCode;

public enum PermissionErrorCode implements ErrorCode {

    PERMISSION_DENIED_NOT_CREATOR,
    PERMISSION_DENIED_NOT_ASSIGN,
    PERMISSION_DENIED_NOT_CREATOR_AND_ASSIGN,
    PERMISSION_DENIED_NOT_CREATOR_AND_ASSIGN_AND_DISPATCHER;


    PermissionErrorCode() {
    }

    @Override
    public String getCode() {
        return this.name().replaceAll("_",".").toLowerCase();
    }
}
