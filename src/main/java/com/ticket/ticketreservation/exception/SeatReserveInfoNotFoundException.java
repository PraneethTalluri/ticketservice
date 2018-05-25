package com.ticket.ticketreservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SeatReserveInfoNotFoundException extends RuntimeException {
    public SeatReserveInfoNotFoundException(Long seatReserveId) {
        super("Seat reserve information not Found. SeatReserveId:" + seatReserveId);
    }
}
