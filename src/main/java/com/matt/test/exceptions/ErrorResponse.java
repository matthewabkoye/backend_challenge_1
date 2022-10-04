package com.matt.test.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class ErrorResponse {
    private HttpStatus status;
    private String description;
    private Long time;
    private String path;
}
