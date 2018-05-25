package com.ticket.ticketreservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class SeatHoldDTO {

    @Getter
    @Setter
    private Long seatHoldId;

    @Getter
    @Setter
    private String venueNumber;

    @Getter
    @Setter
    private String customerEmail;

    @Getter
    @Setter
    private List<SeatDTO> seats = new ArrayList<>();

    public SeatHoldDTO(Long seatHoldId, String venueNumber, String customerEmail) {
        this.seatHoldId = seatHoldId;
        this.venueNumber = venueNumber;
        this.customerEmail = customerEmail;
    }

    public void addSeatDTO(SeatDTO seatDTO){
        seats.add(seatDTO);
    }
}
