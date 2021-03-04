package com.vinicius.produto.errors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentNotValidException.class, InvalidFormatException.class})
    public ErrorResponse methodArgumentNotValidExceptionHandle() {

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Um ou mais campos não foram preenchidos corretamente. Verifique.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponse resourceNotFoundExceptionHandle(ResourceNotFoundException ex){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ResourceInternalErrorServer.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse rsourceInternalErrorServerHandle(ResourceInternalErrorServer ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro Interno");
    }
}
