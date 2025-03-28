package com.tvd.globetrekplatform.api.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class AppException extends RuntimeException {

    HttpStatus httpStatus;

    public AppException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public AppException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public AppException(HttpStatus httpStatus) {
        super("App Exception: " + httpStatus.toString());
        this.httpStatus = httpStatus;
    }
}
