package com.ticket.ticketreservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Row {

    @Id
    @GeneratedValue
    private Long rowId;

    @Getter
    @Setter
    private int rowNumber;

    @JsonIgnore
    @ManyToOne
    @Getter
    private Venue venue;

    @Getter
    @Setter
    @OneToMany(mappedBy = "row")
    private List<Seat> seats = new ArrayList<>();

    public Row(final Venue venue, final int rowNumber){
        this.venue = venue;
        this.rowNumber = rowNumber;
    }

    @Override
    public String toString() {
        return String.format("Row Number: %d (Seats: %s)", rowNumber, seats.toString());
    }
}
