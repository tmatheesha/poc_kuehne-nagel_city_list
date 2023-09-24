package com.kuehne_nagel.city_list.domain.exception;

import com.kuehne_nagel.city_list.application.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.BAD_REQUEST)
public class AuthorizationException extends BaseException {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, String code) {
        super(message, code);
    }
}
