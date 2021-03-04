package com.vinicius.produto.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ResourceInternalErrorServer extends RuntimeException {

    public ResourceInternalErrorServer(String message){
        super(message);
    }
}