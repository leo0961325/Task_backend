package com.link8.tw.controller.advice;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse extends CommonResponse {

    public LoginResponse(boolean success, Object data, String xsrfToken) {
        super(success, data);
        this.xsrfToken = xsrfToken;
    }

    private String xsrfToken;

    public String getXsrfToken() {
        return xsrfToken;
    }

    public void setXsrfToken(String xsrfToken) {
        this.xsrfToken = xsrfToken;
    }
}
