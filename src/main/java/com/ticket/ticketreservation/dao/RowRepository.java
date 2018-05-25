package com.ticket.ticketreservation.dao;

import com.ticket.ticketreservation.model.Row;
import com.ticket.ticketreservation.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RowRepository extends JpaRepository<Row, Long> {
}
