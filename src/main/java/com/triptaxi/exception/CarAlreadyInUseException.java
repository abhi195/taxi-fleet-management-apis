package com.triptaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Car already assigned to another driver.")
public class CarAlreadyInUseException extends Exception {

    private static final long serialVersionUID = -2801223396641103907L;
    
    public CarAlreadyInUseException(String message) {
        super(message);
    }
}
