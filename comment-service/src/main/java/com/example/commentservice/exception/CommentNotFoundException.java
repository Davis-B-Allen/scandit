package com.example.commentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends Exception {

    private HttpStatus httpStatus;

    public CommentNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CommentNotFoundException(String message) {
        super(message);
    }


}
