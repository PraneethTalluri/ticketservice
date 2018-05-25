package com.ticket.ticketreservation.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeatReserve {
    @Id
    @GeneratedValue
    @Getter
    private Long seatReserveId;

    @Getter
    @Setter
    @NonNull
    private String customerEmail;

    @Getter
    @Setter
    @OneToMany(mappedBy = "seatReserve")
    private List<Seat> seats = new ArrayList<>();

    public SeatReserve(final String customerEmail){
        this.customerEmail = customerEmail;
    }


    @Override
    public String toString() {
        return String.format("SeatReserve Number: %d CustomerEmail: %s (Seats: %s)", seatReserveId, customerEmail, seats.toString());
    }

    public void addSeat(Seat seat){
        this.seats.add(seat);
    }
}
