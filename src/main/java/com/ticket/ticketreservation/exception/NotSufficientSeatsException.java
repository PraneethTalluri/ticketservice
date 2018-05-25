package com.ticket.ticketreservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotSufficientSeatsException extends RuntimeException {
    public NotSufficientSeatsException(int numberOfSeatsAvailable) {
        super("Not sufficient seats available. Number of available seats:" + numberOfSeatsAvailable);
    }
}
