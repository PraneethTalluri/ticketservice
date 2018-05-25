package com.ticket.ticketreservation.service;

import com.ticket.ticketreservation.exception.PositiveNumberException;
import com.ticket.ticketreservation.model.Venue;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface VenueService {

    /**
     * Creates new Venue with specified number of rows and seats per row
     *
     * @param numberOfRows number of rows to be created in venue
     * @param seatsPerRow number of seats per row to be created in venue
     * @return Venue with venue number
     * @throws PositiveNumberException
     */
    Venue initializeNewVenue(int numberOfRows, int seatsPerRow) throws PositiveNumberException;

    /**
     * Find all the venues available
     *
     * @return Collection of venue objects
     */
    Collection<Venue> allVenues();

    /**
     * Find venues with specified venue number
     *
     * @param venueNumber Venue Number to find
     * @return Collection of venue objects
     */
    Collection<Venue> getVenueByVenueNumber(String venueNumber);
}
