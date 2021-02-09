package com.link8.tw.controller.advice;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {

    public CommonResponse() {

    }

    public CommonResponse(boolean success, Object data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public CommonResponse(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public CommonResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    private boolean success;
    private Object data;
    private String message;
    private String link;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
