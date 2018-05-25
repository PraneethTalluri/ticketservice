package com.ticket.ticketreservation.dao;

import com.ticket.ticketreservation.model.SeatHold;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatHoldRepository extends JpaRepository<SeatHold, Long> {
}
