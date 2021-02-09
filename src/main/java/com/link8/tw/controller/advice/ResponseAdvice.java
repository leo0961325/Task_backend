package com.link8.tw.controller.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {

    @Autowired
    MessageSource messageSource;


    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return aClass.isAssignableFrom(MappingJackson2HttpMessageConverter.class);
    }

    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof CommonResponse || mediaType.getSubtype().equals("vnd.spring-boot.actuator.v2+json")) {
            return o;
        } else {
            return new CommonResponse(true, o);
        }
    }

    @ExceptionHandler(ErrorCodeException.class)
    @ResponseBody
    ResponseEntity handlerErrorCodeException(HttpServletRequest request, ErrorCodeException e) {
        String message;
        String language = request.getHeader("Language");
        if(language == null) {
            message = messageSource.getMessage(e.getErrorCode().getCode(),null, Locale.TAIWAN);
        } else if (language.equals("zh_TW")) {
            message = messageSource.getMessage(e.getErrorCode().getCode(),null, Locale.TAIWAN);
        } else {
            message = messageSource.getMessage(e.getErrorCode().getCode(),null, Locale.ENGLISH);
        }
        return ResponseEntity.status(500).body(new CommonResponse(false, message));
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    ResponseEntity handlerBindException(BindException e) {
        return ResponseEntity.status(500).body(new CommonResponse(false, ((BindException) e).getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler
    @ResponseBody
    ResponseEntity handlerException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(new CommonResponse(false, e.toString()));
    }
}
