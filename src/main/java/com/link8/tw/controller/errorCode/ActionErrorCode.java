package com.link8.tw.controller.errorCode;

import com.link8.tw.controller.advice.ErrorCode;

public enum ActionErrorCode implements ErrorCode {

    IMAGE_UPLOAD_FAIL,
    IMAGE_GET_FAIL,
    TASK_FINISH_CAN_NOT_EDIT,
    TASK_FINISH_CAN_NOT_REMOVE,
    TASK_NO_ASSIGN_CAN_NOT_PAUSE,
    TASK_NO_ASSIGN_CAN_NOT_START,
    TASK_NO_ASSIGN_CAN_NOT_FINISH,
    TASK_NO_ASSIGN_CAN_NOT_CANCEL,
    TASK_STATUS_CAN_NOT_START,
    TASK_STATUS_CAN_NOT_PAUSE,
    TASK_STATUS_CAN_NOT_CANCEL,
    TASK_NOT_SUB_CAN_NOT_USE;

    ActionErrorCode() {
    }

    @Override
    public String getCode() {
        return this.name().replaceAll("_",".").toLowerCase();
    }
}
