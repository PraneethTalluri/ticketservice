package com.ticket.ticketreservation.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Venue {

    @Id
    @GeneratedValue
    private Long venueId;

    @Getter
    @Setter
    @NonNull
    private String venueNumber;

    @Getter
    @Setter
    @OneToMany(mappedBy = "venue", fetch = FetchType.EAGER)
    private List<Row> rows = new ArrayList<>();

    public Venue(final String venueNumber){
        this.venueNumber = venueNumber;
    }

    @Override
    public String toString() {
        return String.format("Venue Number: %s (Rows: %s)", venueNumber, rows.toString());
    }

}
