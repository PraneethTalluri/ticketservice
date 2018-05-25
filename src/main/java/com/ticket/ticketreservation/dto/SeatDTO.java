package com.ticket.ticketreservation.dto;

import com.ticket.ticketreservation.constants.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class SeatDTO {
    @Getter
    @Setter
    private int rowNumber;

    @Getter
    @Setter
    private int seatNumber;

    @Getter
    @Setter
    private ReservationStatus reservationStatus;


}
