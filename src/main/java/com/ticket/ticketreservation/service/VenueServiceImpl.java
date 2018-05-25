package com.ticket.ticketreservation.service;


import com.ticket.ticketreservation.constants.ReservationStatus;
import com.ticket.ticketreservation.dao.RowRepository;
import com.ticket.ticketreservation.dao.SeatRepository;
import com.ticket.ticketreservation.dao.VenueRepository;
import com.ticket.ticketreservation.exception.PositiveNumberException;
import com.ticket.ticketreservation.exception.VenueDoesNotExistException;
import com.ticket.ticketreservation.model.Row;
import com.ticket.ticketreservation.model.Seat;
import com.ticket.ticketreservation.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class VenueServiceImpl implements VenueService {

    private VenueRepository venueRepository;
    private RowRepository rowRepository;
    private SeatRepository seatRepository;

    @Autowired
    public VenueServiceImpl(VenueRepository venueRepository, RowRepository rowRepository, SeatRepository seatRepository) {
        this.venueRepository = venueRepository;
        this.rowRepository = rowRepository;
        this.seatRepository = seatRepository;
    }

    public Venue initializeNewVenue(int numberOfRows, int seatsPerRow) throws PositiveNumberException {
          if (numberOfRows<=0 || seatsPerRow<=0)
              throw new PositiveNumberException();

          Venue venue = venueRepository.save(new Venue(String.format("Venue%d", venueRepository.countAllByVenueNumberNotNull()+1)));
          for(int i = 1; i<=numberOfRows; i++){
              Row row = rowRepository.save(new Row(venue, i));
              for(int j = 1; j<=seatsPerRow; j++){
                   seatRepository.save(new Seat(row, j, ReservationStatus.AVAILABLE));
              }
          }

          return venue;
    }

    public Collection<Venue> allVenues() {
        Collection<Venue> venues = venueRepository.findAll();
        if(venues.size() == 0)
            throw new VenueDoesNotExistException("N/A");
        return venues;
    }

    public Collection<Venue> getVenueByVenueNumber(String venueNumber) {
        Collection<Venue> venues = venueRepository.findByVenueNumber(venueNumber);
        if(venues.size() == 0)
            throw new VenueDoesNotExistException(venueNumber);
        return venues;
    }
}
