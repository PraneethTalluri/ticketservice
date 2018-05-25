package com.ticket.ticketreservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VenueDoesNotExistException extends RuntimeException {
    public VenueDoesNotExistException(String venueNumber) {
        super("Venue not Found. VenueNumber:" + venueNumber);
    }
}
