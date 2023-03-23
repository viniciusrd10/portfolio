package com.vinicius.bosh.exeception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalErrorException extends  ResponseStatusException {
    public InternalErrorException(HttpStatus code, String message) {
        super(code, message);
    }
}
