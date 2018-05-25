package com.ticket.ticketreservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ticket.ticketreservation.constants.ReservationStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Seat {

    @Id
    @GeneratedValue
    private Long seatId;

    @Getter
    @Setter
    private int seatNumber;

    @JsonIgnore
    @Getter
    @ManyToOne
    private Row row;

    @JsonIgnore
    @ManyToOne
    @Setter
    private SeatHold seatHold;

    @JsonIgnore
    @ManyToOne
    @Setter
    private SeatReserve seatReserve;

    @Getter
    @Setter
    @NonNull
    private ReservationStatus reservationStatus;

    public Seat(final Row row, final int seatNumber, final ReservationStatus reservationStatus){
        this.row = row;
        this.seatNumber = seatNumber;
        this.reservationStatus = reservationStatus;
    }

    @Override
    public String toString() {
        return String.format("Seat Number: %d (Status: %s)", seatNumber, reservationStatus.name());
    }
}
