package com.ticket.ticketreservation.dao;

import com.ticket.ticketreservation.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
