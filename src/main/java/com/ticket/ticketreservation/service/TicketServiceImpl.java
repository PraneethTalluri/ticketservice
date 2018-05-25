package com.ticket.ticketreservation.service;

import com.ticket.ticketreservation.constants.ReservationStatus;
import com.ticket.ticketreservation.dao.*;
import com.ticket.ticketreservation.exception.*;
import com.ticket.ticketreservation.model.Seat;
import com.ticket.ticketreservation.model.SeatHold;
import com.ticket.ticketreservation.model.SeatReserve;
import com.ticket.ticketreservation.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TicketServiceImpl implements TicketService {

    private VenueRepository venueRepository;
    private RowRepository rowRepository;
    private SeatRepository seatRepository;
    private SeatHoldRepository seatHoldRepository;
    private SeatReserveRepository seatReserveRepository;

    private static final int TIMEOUTINSECONDS = 30;

    @Autowired
    public TicketServiceImpl(VenueRepository venueRepository, RowRepository rowRepository, SeatRepository seatRepository, SeatHoldRepository seatHoldRepository, SeatReserveRepository seatReserveRepository) {
        this.venueRepository = venueRepository;
        this.rowRepository = rowRepository;
        this.seatRepository = seatRepository;
        this.seatHoldRepository = seatHoldRepository;
        this.seatReserveRepository = seatReserveRepository;
    }

    public int numSeatsAvailable(String venueNumber) {
        int[] countAvailable = new int[1];
        Collection<Venue> venues = venueRepository.findByVenueNumber(venueNumber);
        if(venues.size() == 0)
            throw new VenueDoesNotExistException(venueNumber);
        venues.forEach(venue -> venue.getRows().forEach(row -> row.getSeats().forEach(seat -> {
            if(seat.getReservationStatus().equals(ReservationStatus.AVAILABLE))
                countAvailable[0]++;
        })));
        return countAvailable[0];
    }

    public SeatHold findAndHoldSeats(String venueNumber, int numberOfSeats, String customerEmail) {
        int numberOfSeatsAvailable = this.numSeatsAvailable(venueNumber);

        if(numberOfSeats<=0)
            throw new PositiveNumberException();

        if(numberOfSeats>numberOfSeatsAvailable)
            throw new NotSufficientSeatsException(numberOfSeatsAvailable);

        List<Seat> chosenSeats = chooseSeats(venueNumber, numberOfSeats);
        SeatHold seatHold = seatHoldRepository.save(new SeatHold(customerEmail));

        chosenSeats.forEach(seat -> {
            seatHold.addSeat(seat);
            seatHoldRepository.save(seatHold);
            seat.setSeatHold(seatHold);
            seat.setReservationStatus(ReservationStatus.HELD);
            seatRepository.save(seat);
        });

        startTimer(seatHold.getSeatHoldId());

        return seatHold;
    }

    public SeatReserve reserveSeats(Long seatHoldId) {
        SeatHold seatHold = this.getSeatHoldInfo(seatHoldId);
        List<Seat> seatsHeld = seatHold.getSeats();

        SeatReserve seatReserve = new SeatReserve(seatHold.getCustomerEmail());

        seatsHeld.forEach(seat -> {
            seatReserve.addSeat(seat);
            seatReserveRepository.save(seatReserve);
            seat.setSeatReserve(seatReserve);
            seat.setReservationStatus(ReservationStatus.RESERVED);
            seat.setSeatHold(null);
            seatRepository.save(seat);
        });
        seatHoldRepository.delete(seatHold);

        return seatReserve;
    }

    private List<Seat> chooseSeats(String venueNumber, int numberOfSeats) {
        Collection<Venue> venues = venueRepository.findByVenueNumber(venueNumber);
        List<Seat> availableSeats = new ArrayList<>();
        int[] countSeats = new int[1];
        venues.forEach(venue -> venue.getRows().forEach(row -> row.getSeats().forEach(seat -> {
            if(countSeats[0]<numberOfSeats && seat.getReservationStatus().equals(ReservationStatus.AVAILABLE)){
                countSeats[0]++;
                availableSeats.add(seat);
            }
        })));
        return availableSeats;
    }

    public SeatHold getSeatHoldInfo(Long seatHoldId){
        if(seatHoldRepository.findById(seatHoldId).isPresent())
            return seatHoldRepository.findById(seatHoldId).get();
        else
            throw new SeatHoldInfoNotFoundException(seatHoldId);
    }

    public List<SeatHold> getAllSeatHolds(){
        return seatHoldRepository.findAll();
    }

    public SeatReserve getSeatReserveInfo(Long seatReserveId){
        if(seatReserveRepository.findById(seatReserveId).isPresent())
            return seatReserveRepository.findById(seatReserveId).get();
        else
            throw new SeatReserveInfoNotFoundException(seatReserveId);
    }

    public List<SeatReserve> getAllReservations(){
        return seatReserveRepository.findAll();
    }

    private void startTimer(Long seatHoldId) {
        Timer timer = new Timer();

        timer.schedule( new TimerTask(){
            public void run() {
                this.releaseSeatsHeld(seatHoldId);
            }

            private void releaseSeatsHeld(Long seatHoldId) {
                SeatHold seatHold;
                if(seatHoldRepository.findById(seatHoldId).isPresent()) {
                    seatHold = seatHoldRepository.findById(seatHoldId).get();
                    List<Seat> seatsHeld = seatHold.getSeats();
                    seatsHeld.forEach(seat -> {
                        seat.setReservationStatus(ReservationStatus.AVAILABLE);
                        seat.setSeatHold(null);
                        seatRepository.save(seat);
                    });
                    seatHoldRepository.delete(seatHold);
                }
            }

        }, TIMEOUTINSECONDS *1000);

    }
}
