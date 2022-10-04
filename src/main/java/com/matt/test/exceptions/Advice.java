package com.matt.test.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class Advice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse resourceNotFoundException(NotFoundException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setDescription(ex.getMessage());
        return response;
    }

    @ExceptionHandler(NoPermissionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse noPermissionFoundException(NoPermissionException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setDescription(ex.getMessage());
        response.setTime(System.currentTimeMillis());
        response.setPath(request.getContextPath());
        return response;
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public ErrorResponse generalException(Exception ex, WebRequest request) {
//        ErrorResponse response = new ErrorResponse();
//        response.setStatus(HttpStatus.EXPECTATION_FAILED);
//        response.setDescription(ex.getMessage());
//        return response;
//    }


}
