package com.ticket.ticketreservation.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeatHold {

    @Id
    @GeneratedValue
    @Getter
    private Long seatHoldId;

    @Getter
    @Setter
    @NonNull
    private String customerEmail;

    @Getter
    @Setter
    @OneToMany(mappedBy = "seatHold", fetch = FetchType.EAGER)
    private List<Seat> seats = new ArrayList<>();

    public SeatHold(final String customerEmail){
        this.customerEmail = customerEmail;
    }


    @Override
    public String toString() {
        return String.format("SeatHold Number: %d CustomerEmail: %s (Seats: %s)", seatHoldId, customerEmail, seats.toString());
    }

    public void addSeat(Seat seat){
        this.seats.add(seat);
    }

}
