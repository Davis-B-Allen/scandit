package com.example.userservice.exception;

import com.netflix.client.http.HttpResponse;
import org.springframework.http.HttpStatus;

public class SignupException extends Exception {
    public SignupException(HttpStatus httpStatus, String message) {
        super(message);
    }
}
