package com.ticket.ticketreservation.dao;

import com.ticket.ticketreservation.model.SeatReserve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatReserveRepository extends JpaRepository<SeatReserve, Long> {
}
