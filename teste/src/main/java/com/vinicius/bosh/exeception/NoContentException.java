package com.vinicius.bosh.exeception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContentException extends ResponseStatusException{
    public NoContentException(HttpStatus code){
        super(code);
    }
}


