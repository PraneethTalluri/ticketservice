package com.ticket.ticketreservation.dao;

import com.ticket.ticketreservation.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface VenueRepository extends JpaRepository<Venue, Long> {

    int countAllByVenueNumberNotNull();

    Collection<Venue> findByVenueNumber(String venueNumber);

}
