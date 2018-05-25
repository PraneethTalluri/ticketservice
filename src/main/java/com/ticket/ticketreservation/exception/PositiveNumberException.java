package com.ticket.ticketreservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PositiveNumberException extends RuntimeException {

    public PositiveNumberException() {
        super("Entered numbers should be positive integers");
    }
}
