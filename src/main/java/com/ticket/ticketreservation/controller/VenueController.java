package com.ticket.ticketreservation.controller;

import com.ticket.ticketreservation.model.Venue;
import com.ticket.ticketreservation.service.VenueService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@Api(value = "Venue API" , description = "To create new venues and get available venues")
@RequestMapping("venue")
public class VenueController {

    @Autowired
    VenueService venueService;

    @ApiOperation(value = "Creates a new venue with specified number of rows and specified number of seats per row")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("/{numberOfRows}/{seatsPerRow}")
    ResponseEntity<?> intializeNewVenue(@ApiParam(value = "number of rows in the venue", required = true) @PathVariable int numberOfRows,
                                        @ApiParam(value = "number of seats per row", required = true) @PathVariable int seatsPerRow){
        Venue venue =  venueService.initializeNewVenue(numberOfRows, seatsPerRow);
        return new ResponseEntity<>(venue, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Gets all venues with seating information")
    @GetMapping
    Collection<Venue> getVenue(){
        return venueService.allVenues();
    }

    @ApiOperation(value = "Gets a venue with seating information")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/{venueNumber}")
    Collection<Venue> getSeatingByVenueNumber(@ApiParam(value = "venue number of the venue", required = true) @PathVariable String venueNumber){
        return venueService.getVenueByVenueNumber(venueNumber);
    }
}
