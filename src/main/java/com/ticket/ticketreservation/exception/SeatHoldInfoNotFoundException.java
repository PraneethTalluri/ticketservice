package com.ticket.ticketreservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SeatHoldInfoNotFoundException extends RuntimeException{
    public SeatHoldInfoNotFoundException(Long seatHoldId) {
        super("Seat hold information not Found. SeatHoldId:" + seatHoldId);
    }
}
