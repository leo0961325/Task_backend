package com.link8.tw.exception.file;

import com.link8.tw.controller.advice.ErrorCode;
import com.link8.tw.controller.advice.ErrorCodeException;

public class FileNotFoundException extends ErrorCodeException {

    public FileNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
