package com.vinicius.bosh.controller.handler;

import com.vinicius.bosh.exeception.ErrorResponse;
import com.vinicius.bosh.exeception.InternalErrorException;
import com.vinicius.bosh.exeception.NoContentException;
import com.vinicius.bosh.exeception.NotFoundException;
import com.vinicius.bosh.utils.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class SubscriptionControllerAdvice {

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public Mono<ResponseEntity<ErrorResponse>> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatusCode().toString(), ex.getReason());
        return Mono.just(new ResponseEntity<>(errorResponse, ex.getStatusCode()));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public Mono<ResponseEntity<ErrorResponse>> handleNotFoundException(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage());
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(InternalErrorException.class)
    @ResponseBody
    public Mono<ResponseEntity<ErrorResponse>> handleNotFoundException(InternalErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ResponseMessage.INTERNAL_ERROR_SERVER.getMessage());
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(NoContentException.class)
    @ResponseBody
    public Mono<ResponseEntity<ErrorResponse>> handleNotFoundException(NoContentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NO_CONTENT.toString());
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.NO_CONTENT));
    }
}
