package com.vinicius.bosh.exeception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends  ResponseStatusException {
    public NotFoundException(HttpStatus code, String message) {
        super(code, message);
    }
}
