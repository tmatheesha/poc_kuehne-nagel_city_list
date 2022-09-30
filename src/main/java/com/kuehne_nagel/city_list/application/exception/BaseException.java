package com.kuehne_nagel.city_list.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR)
public class BaseException extends Exception{
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BaseException(String message) {

        super(message);
    }

    public BaseException(String message, String code) {

        super(message);
        this.code = code;
    }
}
