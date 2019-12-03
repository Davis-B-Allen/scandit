package com.example.commentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CommentNotCreatedException extends Exception {

    public CommentNotCreatedException(String message) {
        super(message);
    }
}
