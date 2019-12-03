package com.example.postservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PostNotCreatedException extends Exception {

    private final HttpStatus httpStatus;

    public PostNotCreatedException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public PostNotCreatedException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
