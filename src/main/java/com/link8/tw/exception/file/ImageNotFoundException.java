package com.link8.tw.exception.file;

import com.link8.tw.controller.advice.ErrorCode;
import com.link8.tw.controller.advice.ErrorCodeException;

public class ImageNotFoundException extends ErrorCodeException {

    public ImageNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
