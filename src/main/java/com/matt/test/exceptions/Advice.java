package com.matt.test.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class Advice {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponse> resourceNotFoundException(NotFoundException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setDescription(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({NoPermissionException.class})
    public ResponseEntity<ErrorResponse> noPermissionFoundException(NoPermissionException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setDescription(ex.getMessage());
        response.setTime(System.currentTimeMillis());
        response.setPath(request.getContextPath());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generalException(Exception ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.EXPECTATION_FAILED);
        response.setDescription(ex.getMessage());
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
    }


}
