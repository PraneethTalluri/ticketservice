package com.ticket.ticketreservation;

import com.ticket.ticketreservation.constants.ReservationStatus;
import com.ticket.ticketreservation.dao.RowRepository;
import com.ticket.ticketreservation.dao.SeatRepository;
import com.ticket.ticketreservation.dao.VenueRepository;
import com.ticket.ticketreservation.model.Row;
import com.ticket.ticketreservation.model.Seat;
import com.ticket.ticketreservation.model.Venue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketreservationApplication.class)
@WebAppConfiguration
public class VenueControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;


    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Venue> venues = new ArrayList<>();

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private RowRepository rowRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters){
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup(){
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.seatRepository.deleteAllInBatch();
        this.rowRepository.deleteAllInBatch();
        this.venueRepository.deleteAllInBatch();

        Venue venue1 = venueRepository.save(new Venue(String.format("Venue%d", venueRepository.countAllByVenueNumberNotNull()+1)));
        venues.add(venue1);

        List<Row> rows1 = new ArrayList<>();
        Row row11 = rowRepository.save(new Row(venue1, 1));
        rows1.add(row11);
        List<Seat> seats11 = new ArrayList<>();
        Seat seat111 = seatRepository.save(new Seat(row11, 1, ReservationStatus.AVAILABLE));
        seats11.add(seat111);
        Seat seat112 = seatRepository.save(new Seat(row11, 2, ReservationStatus.AVAILABLE));
        seats11.add(seat112);
        row11.setSeats(seats11);

        Row row12 = rowRepository.save(new Row(venue1, 2));
        rows1.add(row12);
        List<Seat> seats12 = new ArrayList<>();
        Seat seat121 = seatRepository.save(new Seat(row12, 1, ReservationStatus.AVAILABLE));
        seats12.add(seat121);
        Seat seat122 = seatRepository.save(new Seat(row12, 2, ReservationStatus.AVAILABLE));
        seats12.add(seat122);
        row12.setSeats(seats12);

        venue1.setRows(rows1);

        Venue venue2 = venueRepository.save(new Venue(String.format("Venue%d", venueRepository.countAllByVenueNumberNotNull()+1)));
        venues.add(venue2);

        List<Row> rows2 = new ArrayList<>();
        Row row21 = rowRepository.save(new Row(venue2, 1));
        rows2.add(row21);
        List<Seat> seats21 = new ArrayList<>();
        Seat seat211 = seatRepository.save(new Seat(row21, 1, ReservationStatus.AVAILABLE));
        seats21.add(seat211);
        Seat seat212 = seatRepository.save(new Seat(row21, 2, ReservationStatus.AVAILABLE));
        seats21.add(seat212);
        row21.setSeats(seats21);

        Row row22 = rowRepository.save(new Row(venue2, 2));
        rows2.add(row22);
        List<Seat> seats22 = new ArrayList<>();
        Seat seat221 = seatRepository.save(new Seat(row22, 1, ReservationStatus.AVAILABLE));
        seats22.add(seat221);
        Seat seat222 = seatRepository.save(new Seat(row22, 2, ReservationStatus.AVAILABLE));
        seats22.add(seat222);
        row22.setSeats(seats22);

        venue2.setRows(rows2);
    }

    @Test
    public void venueNotFound() throws Exception{
        mockMvc.perform(get("/venue/venue1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void numberOfRowOrSeatsNotValid() throws Exception{
        mockMvc.perform(post("/venue/0/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createVenueWithNumberOfRowAndSeats() throws Exception{
        mockMvc.perform(post("/venue/2/3"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.venueNumber", is("Venue3")));
    }

    @Test
    public void findSpecifiedVenue() throws Exception{
        mockMvc.perform(get("/venue/Venue1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].venueNumber", is("Venue1")))
                .andExpect(jsonPath("$[0].rows", hasSize(venues.get(0).getRows().size())))
                .andExpect(jsonPath("$[0].rows[0].rowNumber", is(venues.get(0).getRows().get(0).getRowNumber())))
                .andExpect(jsonPath("$[0].rows[0].seats", hasSize(venues.get(0).getRows().get(0).getSeats().size())))
                .andExpect(jsonPath("$[0].rows[0].seats[0].seatNumber", is(venues.get(0).getRows().get(0).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$[0].rows[0].seats[0].reservationStatus", is(venues.get(0).getRows().get(0).getSeats().get(0).getReservationStatus().name())))
                .andExpect(jsonPath("$[0].rows[0].seats[1].seatNumber", is(venues.get(0).getRows().get(0).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$[0].rows[0].seats[1].reservationStatus", is(venues.get(0).getRows().get(0).getSeats().get(1).getReservationStatus().name())))
                .andExpect(jsonPath("$[0].rows[1].rowNumber", is(venues.get(0).getRows().get(1).getRowNumber())))
                .andExpect(jsonPath("$[0].rows[1].seats", hasSize(venues.get(0).getRows().get(1).getSeats().size())))
                .andExpect(jsonPath("$[0].rows[1].seats[0].seatNumber", is(venues.get(0).getRows().get(1).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$[0].rows[1].seats[0].reservationStatus", is(venues.get(0).getRows().get(1).getSeats().get(0).getReservationStatus().name())))
                .andExpect(jsonPath("$[0].rows[1].seats[1].seatNumber", is(venues.get(0).getRows().get(1).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$[0].rows[1].seats[1].reservationStatus", is(venues.get(0).getRows().get(1).getSeats().get(1).getReservationStatus().name())));
    }

    @Test
    public void findAllVenues() throws Exception{
        mockMvc.perform(get("/venue"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].venueNumber", is("Venue1")))
                .andExpect(jsonPath("$[0].rows", hasSize(venues.get(0).getRows().size())))
                .andExpect(jsonPath("$[0].rows[0].rowNumber", is(venues.get(0).getRows().get(0).getRowNumber())))
                .andExpect(jsonPath("$[0].rows[0].seats", hasSize(venues.get(0).getRows().get(0).getSeats().size())))
                .andExpect(jsonPath("$[0].rows[0].seats[0].seatNumber", is(venues.get(0).getRows().get(0).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$[0].rows[0].seats[0].reservationStatus", is(venues.get(0).getRows().get(0).getSeats().get(0).getReservationStatus().name())))
                .andExpect(jsonPath("$[0].rows[0].seats[1].seatNumber", is(venues.get(0).getRows().get(0).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$[0].rows[0].seats[1].reservationStatus", is(venues.get(0).getRows().get(0).getSeats().get(1).getReservationStatus().name())))
                .andExpect(jsonPath("$[0].rows[1].rowNumber", is(venues.get(0).getRows().get(1).getRowNumber())))
                .andExpect(jsonPath("$[0].rows[1].seats", hasSize(venues.get(0).getRows().get(1).getSeats().size())))
                .andExpect(jsonPath("$[0].rows[1].seats[0].seatNumber", is(venues.get(0).getRows().get(1).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$[0].rows[1].seats[0].reservationStatus", is(venues.get(0).getRows().get(1).getSeats().get(0).getReservationStatus().name())))
                .andExpect(jsonPath("$[0].rows[1].seats[1].seatNumber", is(venues.get(0).getRows().get(1).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$[0].rows[1].seats[1].reservationStatus", is(venues.get(0).getRows().get(1).getSeats().get(1).getReservationStatus().name())))
                .andExpect(jsonPath("$[1].venueNumber", is("Venue2")))
                .andExpect(jsonPath("$[1].rows", hasSize(venues.get(1).getRows().size())))
                .andExpect(jsonPath("$[1].rows[0].rowNumber", is(venues.get(1).getRows().get(0).getRowNumber())))
                .andExpect(jsonPath("$[1].rows[0].seats", hasSize(venues.get(1).getRows().get(0).getSeats().size())))
                .andExpect(jsonPath("$[1].rows[0].seats[0].seatNumber", is(venues.get(1).getRows().get(0).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$[1].rows[0].seats[0].reservationStatus", is(venues.get(1).getRows().get(0).getSeats().get(0).getReservationStatus().name())))
                .andExpect(jsonPath("$[1].rows[0].seats[1].seatNumber", is(venues.get(1).getRows().get(0).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$[1].rows[0].seats[1].reservationStatus", is(venues.get(1).getRows().get(0).getSeats().get(1).getReservationStatus().name())))
                .andExpect(jsonPath("$[1].rows[1].rowNumber", is(venues.get(1).getRows().get(1).getRowNumber())))
                .andExpect(jsonPath("$[1].rows[1].seats", hasSize(venues.get(1).getRows().get(1).getSeats().size())))
                .andExpect(jsonPath("$[1].rows[1].seats[0].seatNumber", is(venues.get(1).getRows().get(1).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$[1].rows[1].seats[0].reservationStatus", is(venues.get(1).getRows().get(1).getSeats().get(0).getReservationStatus().name())))
                .andExpect(jsonPath("$[1].rows[1].seats[1].seatNumber", is(venues.get(1).getRows().get(1).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$[1].rows[1].seats[1].reservationStatus", is(venues.get(1).getRows().get(1).getSeats().get(1).getReservationStatus().name())));
    }
}
