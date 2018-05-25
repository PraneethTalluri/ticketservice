package com.ticket.ticketreservation.service;

import com.ticket.ticketreservation.model.SeatHold;
import com.ticket.ticketreservation.model.SeatReserve;

import java.util.List;

public interface TicketService {

    /**
     * The number of seats in the venue that are neither held nor reserved
     *
     * @param venueNumber venue number of the venue
     * @return the number of tickets available in the venue
     */
    int numSeatsAvailable(String venueNumber);

    /**
     * Find and hold the best available seats for a customer
     *
     * @param venueNumber venue number of the venue
     * @param numSeats the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related information
     */
    SeatHold findAndHoldSeats(String venueNumber, int numSeats, String customerEmail);

    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId the seat hold identifier
     * @return a reservation confirmation code with seats reserved
     */
    SeatReserve reserveSeats(Long seatHoldId);

    /**
     * Find specific seat hold information
     *
     * @param seatHoldId the seat hold identifier
     * @return a SeatHold object identifying the specific seats and related information
     */
    SeatHold getSeatHoldInfo(Long seatHoldId);

    /**
     * Find all the seat holds in all venues
     *
     * @return SeatHold objects with identifying the specific seats and related information
     */
    List<SeatHold> getAllSeatHolds();

    /**
     * Find specific reservation information
     *
     * @param seatReserveId the confirmation number
     * @return a SeatReserve object identifying the specific seats and related information
     */
    SeatReserve getSeatReserveInfo(Long seatReserveId);

    /**
     * Find all the reservations in all venues
     *
     * @return SeatReserve objects identifying the specific seats and related information
     */
    List<SeatReserve> getAllReservations();


}
