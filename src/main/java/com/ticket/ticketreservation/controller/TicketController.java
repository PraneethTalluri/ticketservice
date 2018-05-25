package com.ticket.ticketreservation.controller;

import com.ticket.ticketreservation.dto.SeatDTO;
import com.ticket.ticketreservation.dto.SeatHoldDTO;
import com.ticket.ticketreservation.dto.SeatReserveDTO;
import com.ticket.ticketreservation.model.SeatHold;
import com.ticket.ticketreservation.model.SeatReserve;
import com.ticket.ticketreservation.service.TicketService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(value = "Tickets Reservation API", description = "To find and make seat hold/reservations")
@RequestMapping("tickets")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @ApiOperation(value = "Gets number of seats available at specified venue")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/{venueNumber}")
    int getVenueAvailableSeatsCount(@ApiParam(value = "venue number of the venue", required = true) @PathVariable String venueNumber){
        return ticketService.numSeatsAvailable(venueNumber);
    }

    @ApiOperation(value = "Creates a new seat hold within specified venue, number of seats to be held and email of the customer")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("seatHold/{venueNumber}/{numberOfSeats}/{customerEmail}")
    ResponseEntity<?> holdSeats(@ApiParam(value = "venue number of the venue", required = true) @PathVariable String venueNumber,
                                @ApiParam(value = "number of seats to be held", required = true) @PathVariable int numberOfSeats,
                                @ApiParam(value = "email of the customer", required = true) @PathVariable String customerEmail){
        SeatHold seatHold = ticketService.findAndHoldSeats(venueNumber, numberOfSeats,customerEmail);
        SeatHoldDTO seatHoldDTO = new SeatHoldDTO(seatHold.getSeatHoldId(), venueNumber, seatHold.getCustomerEmail());
        seatHold.getSeats().forEach(seat ->
                seatHoldDTO.addSeatDTO(new SeatDTO(seat.getRow().getRowNumber(), seat.getSeatNumber(), seat.getReservationStatus())));
        return new ResponseEntity<>(seatHoldDTO, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Gets the seat hold information of the specified seatHoldId")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/seatHold/{seatHoldId}")
    SeatHoldDTO getSeatHoldInfo(@ApiParam(value = "Id of the seat hold to find", required = true) @PathVariable Long seatHoldId){
        SeatHold seatHold = ticketService.getSeatHoldInfo(seatHoldId);
        SeatHoldDTO seatHoldDTO = new SeatHoldDTO(seatHold.getSeatHoldId(), seatHold.getSeats().get(0).getRow().getVenue().getVenueNumber() ,seatHold.getCustomerEmail());
        seatHold.getSeats().forEach(seat ->
                seatHoldDTO.addSeatDTO(new SeatDTO(seat.getRow().getRowNumber(), seat.getSeatNumber(), seat.getReservationStatus())));
        return seatHoldDTO;
    }

    @ApiOperation(value = "Gets all the seat holds with their information")
    @GetMapping("/seatHold")
    List<SeatHoldDTO> getAllSeatHolds(){
        List<SeatHold> seatHolds = ticketService.getAllSeatHolds();
        List<SeatHoldDTO> seatHoldDTOs = new ArrayList<>();
        seatHolds.forEach(seatHold -> {
            SeatHoldDTO seatHoldDTO = new SeatHoldDTO(seatHold.getSeatHoldId(), seatHold.getSeats().get(0).getRow().getVenue().getVenueNumber() ,seatHold.getCustomerEmail());
            seatHold.getSeats().forEach(seat ->
                    seatHoldDTO.addSeatDTO(new SeatDTO(seat.getRow().getRowNumber(), seat.getSeatNumber(), seat.getReservationStatus())));
            seatHoldDTOs.add(seatHoldDTO);
        });
        return seatHoldDTOs;
    }

    @ApiOperation(value = "Reserves seats with specified seatholdId")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("reserve/{seatHoldId}")
    ResponseEntity<?> reserveSeats(@ApiParam(value = "id of the seat hold to be reserved", required = true) @PathVariable Long seatHoldId){
        SeatReserve seatReserve = ticketService.reserveSeats(seatHoldId);
        SeatReserveDTO seatReserveDTO = new SeatReserveDTO(seatReserve.getSeatReserveId(), seatReserve.getSeats().get(0).getRow().getVenue().getVenueNumber(), seatReserve.getCustomerEmail());
        seatReserve.getSeats().forEach(seat ->
                seatReserveDTO.addSeatDTO(new SeatDTO(seat.getRow().getRowNumber(), seat.getSeatNumber(), seat.getReservationStatus())));
        return new ResponseEntity<>(seatReserveDTO, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Gets reservation information of the specified confirmation number")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/reserve/{confirmationNumber}")
    SeatReserveDTO getSeatReserveInfo(@ApiParam(value = "confirmation number of the reservation made", required = true) @PathVariable Long confirmationNumber){
        SeatReserve seatReserve = ticketService.getSeatReserveInfo(confirmationNumber);
        SeatReserveDTO seatReserveDTO = new SeatReserveDTO(seatReserve.getSeatReserveId(), seatReserve.getSeats().get(0).getRow().getVenue().getVenueNumber(), seatReserve.getCustomerEmail());
        seatReserve.getSeats().forEach(seat ->
                seatReserveDTO.addSeatDTO(new SeatDTO(seat.getRow().getRowNumber(), seat.getSeatNumber(), seat.getReservationStatus())));
        return seatReserveDTO;
    }

    @ApiOperation(value = "Gets all reservations with their information")
    @GetMapping("/reserve")
    List<SeatReserveDTO> getAllReservations(){
        List<SeatReserve> seatReserves = ticketService.getAllReservations();
        List<SeatReserveDTO> seatReserveDTOs = new ArrayList<>();
        seatReserves.forEach(seatReserve -> {
            SeatReserveDTO seatReserveDTO = new SeatReserveDTO(seatReserve.getSeatReserveId(), seatReserve.getSeats().get(0).getRow().getVenue().getVenueNumber(), seatReserve.getCustomerEmail());
            seatReserve.getSeats().forEach(seat ->
                    seatReserveDTO.addSeatDTO(new SeatDTO(seat.getRow().getRowNumber(), seat.getSeatNumber(), seat.getReservationStatus())));
            seatReserveDTOs.add(seatReserveDTO);
        });
        return seatReserveDTOs;
    }
}
