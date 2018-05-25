package com.ticket.ticketreservation;

import com.ticket.ticketreservation.constants.ReservationStatus;
import com.ticket.ticketreservation.dao.*;
import com.ticket.ticketreservation.model.*;
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
public class TicketControllerTest {

    private static final int TIMEOUTINSECONDS = 10;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Venue> venues = new ArrayList<>();
    private List<SeatHold> seatHolds = new ArrayList<>();
    private List<SeatReserve> seatReserves = new ArrayList<>();

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private RowRepository rowRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatHoldRepository seatHoldRepository;

    @Autowired
    private SeatReserveRepository seatReserveRepository;

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
        this.seatHoldRepository.deleteAllInBatch();
        this.seatReserveRepository.deleteAllInBatch();
        this.venueRepository.deleteAllInBatch();

        Venue venue1 = venueRepository.save(new Venue(String.format("Venue%d", venueRepository.countAllByVenueNumberNotNull()+1)));
        venues.add(venue1);

        List<Row> rows1 = new ArrayList<>();
        Row row11 = rowRepository.save(new Row(venue1, 1));
        rows1.add(row11);
        List<Seat> seats11 = new ArrayList<>();
        SeatHold seatHold1 = seatHoldRepository.save(new SeatHold("SeatHold1Venue1@test.com"));
        SeatReserve seatReserve1 = seatReserveRepository.save(new SeatReserve("SeatReserve1Venue1@test.com"));
        Seat seat111 = seatRepository.save(new Seat(row11, 1, ReservationStatus.HELD));
        seats11.add(seat111);
        seatHold1.addSeat(seat111);
        seatHoldRepository.save(seatHold1);
        seat111.setSeatHold(seatHold1);
        seatRepository.save(seat111);
        Seat seat112 = seatRepository.save(new Seat(row11, 2, ReservationStatus.HELD));
        seats11.add(seat112);
        seatHold1.addSeat(seat112);
        seatHoldRepository.save(seatHold1);
        seat112.setSeatHold(seatHold1);
        seatRepository.save(seat112);
        Seat seat113 = seatRepository.save(new Seat(row11, 3, ReservationStatus.HELD));
        seats11.add(seat113);
        seatHold1.addSeat(seat113);
        seatHoldRepository.save(seatHold1);
        seat113.setSeatHold(seatHold1);
        seatRepository.save(seat113);
        Seat seat114 = seatRepository.save(new Seat(row11, 4, ReservationStatus.AVAILABLE));
        seats11.add(seat114);
        Seat seat115 = seatRepository.save(new Seat(row11, 5, ReservationStatus.RESERVED));
        seats11.add(seat115);
        seatReserve1.addSeat(seat115);
        seatReserveRepository.save(seatReserve1);
        seat115.setSeatReserve(seatReserve1);
        seatRepository.save(seat115);
        Seat seat116 = seatRepository.save(new Seat(row11, 6, ReservationStatus.RESERVED));
        seats11.add(seat116);
        seatReserve1.addSeat(seat116);
        seatReserveRepository.save(seatReserve1);
        seat116.setSeatReserve(seatReserve1);
        seatRepository.save(seat116);
        row11.setSeats(seats11);
        seatHolds.add(seatHold1);
        seatReserves.add(seatReserve1);

        Row row12 = rowRepository.save(new Row(venue1, 2));
        rows1.add(row12);
        List<Seat> seats12 = new ArrayList<>();
        SeatHold seatHold2 = seatHoldRepository.save(new SeatHold("SeatHold2Venue1@test.com"));
        SeatReserve seatReserve2 = seatReserveRepository.save(new SeatReserve("SeatReserve2Venue1@test.com"));
        Seat seat121 = seatRepository.save(new Seat(row12, 1, ReservationStatus.AVAILABLE));
        seats12.add(seat121);
        Seat seat122 = seatRepository.save(new Seat(row12, 2, ReservationStatus.AVAILABLE));
        seats12.add(seat122);
        Seat seat123 = seatRepository.save(new Seat(row12, 3, ReservationStatus.HELD));
        seats12.add(seat123);
        seatHold2.addSeat(seat123);
        seatHoldRepository.save(seatHold2);
        seat123.setSeatHold(seatHold2);
        seatRepository.save(seat123);
        Seat seat124 = seatRepository.save(new Seat(row12, 4, ReservationStatus.HELD));
        seats12.add(seat124);
        seatHold2.addSeat(seat124);
        seatHoldRepository.save(seatHold2);
        seat124.setSeatHold(seatHold2);
        seatRepository.save(seat124);
        Seat seat125 = seatRepository.save(new Seat(row12, 5, ReservationStatus.RESERVED));
        seats12.add(seat125);
        seatReserve2.addSeat(seat125);
        seatReserveRepository.save(seatReserve2);
        seat125.setSeatReserve(seatReserve2);
        seatRepository.save(seat125);
        Seat seat126 = seatRepository.save(new Seat(row12, 6, ReservationStatus.RESERVED));
        seats12.add(seat126);
        seatReserve2.addSeat(seat126);
        seatReserveRepository.save(seatReserve2);
        seat126.setSeatReserve(seatReserve2);
        seatRepository.save(seat126);
        row12.setSeats(seats12);
        seatHolds.add(seatHold2);
        seatReserves.add(seatReserve2);

        venue1.setRows(rows1);

        Venue venue2 = venueRepository.save(new Venue(String.format("Venue%d", venueRepository.countAllByVenueNumberNotNull()+1)));
        venues.add(venue2);

        List<Row> rows2 = new ArrayList<>();
        Row row21 = rowRepository.save(new Row(venue2, 1));
        rows2.add(row21);
        List<Seat> seats21 = new ArrayList<>();
        SeatHold seatHold3 = seatHoldRepository.save(new SeatHold("SeatHold3Venue2@test.com"));
        SeatReserve seatReserve3 = seatReserveRepository.save(new SeatReserve("SeatReserve3Venue2@test.com"));
        Seat seat211 = seatRepository.save(new Seat(row21, 1, ReservationStatus.HELD));
        seats21.add(seat211);
        seatHold3.addSeat(seat211);
        seatHoldRepository.save(seatHold3);
        seat211.setSeatHold(seatHold3);
        seatRepository.save(seat211);
        Seat seat212 = seatRepository.save(new Seat(row21, 2, ReservationStatus.RESERVED));
        seats21.add(seat212);
        seatReserve3.addSeat(seat212);
        seatReserveRepository.save(seatReserve3);
        seat212.setSeatReserve(seatReserve3);
        seatRepository.save(seat212);
        row21.setSeats(seats21);
        seatHolds.add(seatHold3);
        seatReserves.add(seatReserve3);

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
    public void numberOfSeatsVenueNotFound() throws Exception{
        mockMvc.perform(get("/tickets/venue1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void seatHoldVenueNotFound() throws Exception{
        mockMvc.perform(post("/tickets/seatHold/venue1/2/abc@gmail.com"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void numberOfSeatsNotValid() throws Exception{
        mockMvc.perform(post("/tickets/seatHold/Venue1/-1/abc@gmail.com"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void numberOfSeatsGreaterThanAvailable() throws Exception{
        mockMvc.perform(post("/tickets/seatHold/Venue1/5/abc@gmail.com"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void seatHoldNotAvailableForReservation() throws Exception{
        mockMvc.perform(post("/tickets/reserve/131"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void seatHoldNotAvailableForSeatHoldInfo() throws Exception{
        mockMvc.perform(get("/tickets/seatHold/131"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void numberOfAvailableSeats() throws Exception{
        mockMvc.perform(get("/tickets/Venue1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", is(3)));
    }

    @Test
    public void findSpecifiedSeatHold() throws Exception{
        mockMvc.perform(get("/tickets/seatHold/"+seatHolds.get(0).getSeatHoldId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                //.andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.seatHoldId", is(seatHolds.get(0).getSeatHoldId().intValue())))
                .andExpect(jsonPath("$.venueNumber", is(seatHolds.get(0).getSeats().get(0).getRow().getVenue().getVenueNumber())))
                .andExpect(jsonPath("$.customerEmail", is(seatHolds.get(0).getCustomerEmail())))
                .andExpect(jsonPath("$.seats", hasSize(seatHolds.get(0).getSeats().size())))
                .andExpect(jsonPath("$.seats[0].rowNumber", is(seatHolds.get(0).getSeats().get(0).getRow().getRowNumber())))
                .andExpect(jsonPath("$.seats[0].seatNumber", is(seatHolds.get(0).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$.seats[0].reservationStatus", is(seatHolds.get(0).getSeats().get(0).getReservationStatus().name())))
                .andExpect(jsonPath("$.seats[1].rowNumber", is(seatHolds.get(0).getSeats().get(1).getRow().getRowNumber())))
                .andExpect(jsonPath("$.seats[1].seatNumber", is(seatHolds.get(0).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$.seats[1].reservationStatus", is(seatHolds.get(0).getSeats().get(1).getReservationStatus().name())))
                .andExpect(jsonPath("$.seats[2].rowNumber", is(seatHolds.get(0).getSeats().get(2).getRow().getRowNumber())))
                .andExpect(jsonPath("$.seats[2].seatNumber", is(seatHolds.get(0).getSeats().get(2).getSeatNumber())))
                .andExpect(jsonPath("$.seats[2].reservationStatus", is(seatHolds.get(0).getSeats().get(2).getReservationStatus().name())));
    }

    @Test
    public void findAllSeatHold() throws Exception{
        mockMvc.perform(get("/tickets/seatHold"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].seatHoldId", is(seatHolds.get(0).getSeatHoldId().intValue())))
                .andExpect(jsonPath("$[0].venueNumber", is(seatHolds.get(0).getSeats().get(0).getRow().getVenue().getVenueNumber())))
                .andExpect(jsonPath("$[0].customerEmail", is(seatHolds.get(0).getCustomerEmail())))
                .andExpect(jsonPath("$[0].seats", hasSize(seatHolds.get(0).getSeats().size())))
                .andExpect(jsonPath("$[0].seats[0].rowNumber", is(seatHolds.get(0).getSeats().get(0).getRow().getRowNumber())))
                .andExpect(jsonPath("$[0].seats[0].seatNumber", is(seatHolds.get(0).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$[0].seats[0].reservationStatus", is(seatHolds.get(0).getSeats().get(0).getReservationStatus().name())))
                .andExpect(jsonPath("$[0].seats[1].rowNumber", is(seatHolds.get(0).getSeats().get(1).getRow().getRowNumber())))
                .andExpect(jsonPath("$[0].seats[1].seatNumber", is(seatHolds.get(0).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$[0].seats[1].reservationStatus", is(seatHolds.get(0).getSeats().get(1).getReservationStatus().name())))
                .andExpect(jsonPath("$[0].seats[2].rowNumber", is(seatHolds.get(0).getSeats().get(2).getRow().getRowNumber())))
                .andExpect(jsonPath("$[0].seats[2].seatNumber", is(seatHolds.get(0).getSeats().get(2).getSeatNumber())))
                .andExpect(jsonPath("$[0].seats[2].reservationStatus", is(seatHolds.get(0).getSeats().get(2).getReservationStatus().name())))
                .andExpect(jsonPath("$[1].seatHoldId", is(seatHolds.get(1).getSeatHoldId().intValue())))
                .andExpect(jsonPath("$[1].venueNumber", is(seatHolds.get(1).getSeats().get(0).getRow().getVenue().getVenueNumber())))
                .andExpect(jsonPath("$[1].customerEmail", is(seatHolds.get(1).getCustomerEmail())))
                .andExpect(jsonPath("$[1].seats", hasSize(seatHolds.get(1).getSeats().size())))
                .andExpect(jsonPath("$[1].seats[0].rowNumber", is(seatHolds.get(1).getSeats().get(0).getRow().getRowNumber())))
                .andExpect(jsonPath("$[1].seats[0].seatNumber", is(seatHolds.get(1).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$[1].seats[0].reservationStatus", is(seatHolds.get(1).getSeats().get(0).getReservationStatus().name())))
                .andExpect(jsonPath("$[1].seats[1].rowNumber", is(seatHolds.get(1).getSeats().get(1).getRow().getRowNumber())))
                .andExpect(jsonPath("$[1].seats[1].seatNumber", is(seatHolds.get(1).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$[1].seats[1].reservationStatus", is(seatHolds.get(1).getSeats().get(1).getReservationStatus().name())))
                .andExpect(jsonPath("$[2].seatHoldId", is(seatHolds.get(2).getSeatHoldId().intValue())))
                .andExpect(jsonPath("$[2].venueNumber", is(seatHolds.get(2).getSeats().get(0).getRow().getVenue().getVenueNumber())))
                .andExpect(jsonPath("$[2].customerEmail", is(seatHolds.get(2).getCustomerEmail())))
                .andExpect(jsonPath("$[2].seats", hasSize(seatHolds.get(2).getSeats().size())))
                .andExpect(jsonPath("$[2].seats[0].rowNumber", is(seatHolds.get(2).getSeats().get(0).getRow().getRowNumber())))
                .andExpect(jsonPath("$[2].seats[0].seatNumber", is(seatHolds.get(2).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$[2].seats[0].reservationStatus", is(seatHolds.get(2).getSeats().get(0).getReservationStatus().name())));
    }

    @Test
    public void findSpecifiedReservation() throws Exception{
        mockMvc.perform(get("/tickets/reserve/"+seatReserves.get(0).getSeatReserveId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                //.andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.confirmationNumber", is(seatReserves.get(0).getSeatReserveId().intValue())))
                .andExpect(jsonPath("$.venueNumber", is(seatReserves.get(0).getSeats().get(0).getRow().getVenue().getVenueNumber())))
                .andExpect(jsonPath("$.customerEmail", is(seatReserves.get(0).getCustomerEmail())))
                .andExpect(jsonPath("$.seats", hasSize(seatReserves.get(0).getSeats().size())))
                .andExpect(jsonPath("$.seats[0].rowNumber", is(seatReserves.get(0).getSeats().get(0).getRow().getRowNumber())))
                .andExpect(jsonPath("$.seats[0].seatNumber", is(seatReserves.get(0).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$.seats[0].reservationStatus", is(seatReserves.get(0).getSeats().get(0).getReservationStatus().name())))
                .andExpect(jsonPath("$.seats[1].rowNumber", is(seatReserves.get(0).getSeats().get(1).getRow().getRowNumber())))
                .andExpect(jsonPath("$.seats[1].seatNumber", is(seatReserves.get(0).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$.seats[1].reservationStatus", is(seatReserves.get(0).getSeats().get(1).getReservationStatus().name())));
    }

    @Test
    public void findAllReservations() throws Exception{
        mockMvc.perform(get("/tickets/reserve"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].confirmationNumber", is(seatReserves.get(0).getSeatReserveId().intValue())))
                .andExpect(jsonPath("$[0].venueNumber", is(seatReserves.get(0).getSeats().get(0).getRow().getVenue().getVenueNumber())))
                .andExpect(jsonPath("$[0].customerEmail", is(seatReserves.get(0).getCustomerEmail())))
                .andExpect(jsonPath("$[0].seats", hasSize(seatReserves.get(0).getSeats().size())))
                .andExpect(jsonPath("$[0].seats[0].rowNumber", is(seatReserves.get(0).getSeats().get(0).getRow().getRowNumber())))
                .andExpect(jsonPath("$[0].seats[0].seatNumber", is(seatReserves.get(0).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$[0].seats[0].reservationStatus", is(seatReserves.get(0).getSeats().get(0).getReservationStatus().name())))
                .andExpect(jsonPath("$[0].seats[1].rowNumber", is(seatReserves.get(0).getSeats().get(1).getRow().getRowNumber())))
                .andExpect(jsonPath("$[0].seats[1].seatNumber", is(seatReserves.get(0).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$[0].seats[1].reservationStatus", is(seatReserves.get(0).getSeats().get(1).getReservationStatus().name())))
                .andExpect(jsonPath("$[1].confirmationNumber", is(seatReserves.get(1).getSeatReserveId().intValue())))
                .andExpect(jsonPath("$[1].venueNumber", is(seatReserves.get(1).getSeats().get(0).getRow().getVenue().getVenueNumber())))
                .andExpect(jsonPath("$[1].customerEmail", is(seatReserves.get(1).getCustomerEmail())))
                .andExpect(jsonPath("$[1].seats", hasSize(seatReserves.get(1).getSeats().size())))
                .andExpect(jsonPath("$[1].seats[0].rowNumber", is(seatReserves.get(1).getSeats().get(0).getRow().getRowNumber())))
                .andExpect(jsonPath("$[1].seats[0].seatNumber", is(seatReserves.get(1).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$[1].seats[0].reservationStatus", is(seatReserves.get(1).getSeats().get(0).getReservationStatus().name())))
                .andExpect(jsonPath("$[1].seats[1].rowNumber", is(seatReserves.get(1).getSeats().get(1).getRow().getRowNumber())))
                .andExpect(jsonPath("$[1].seats[1].seatNumber", is(seatReserves.get(1).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$[1].seats[1].reservationStatus", is(seatReserves.get(1).getSeats().get(1).getReservationStatus().name())))
                .andExpect(jsonPath("$[2].confirmationNumber", is(seatReserves.get(2).getSeatReserveId().intValue())))
                .andExpect(jsonPath("$[2].venueNumber", is(seatReserves.get(2).getSeats().get(0).getRow().getVenue().getVenueNumber())))
                .andExpect(jsonPath("$[2].customerEmail", is(seatReserves.get(2).getCustomerEmail())))
                .andExpect(jsonPath("$[2].seats", hasSize(seatReserves.get(2).getSeats().size())))
                .andExpect(jsonPath("$[2].seats[0].rowNumber", is(seatReserves.get(2).getSeats().get(0).getRow().getRowNumber())))
                .andExpect(jsonPath("$[2].seats[0].seatNumber", is(seatReserves.get(2).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$[2].seats[0].reservationStatus", is(seatReserves.get(2).getSeats().get(0).getReservationStatus().name())));
    }

    @Test
    public void createNewSeatHold() throws Exception{
        mockMvc.perform(post("/tickets/seatHold/Venue1/3/SeatHold4Venue1@test.com"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.venueNumber", is("Venue1")))
                .andExpect(jsonPath("$.customerEmail", is("SeatHold4Venue1@test.com")))
                .andExpect(jsonPath("$.seats", hasSize(3)))
                .andExpect(jsonPath("$.seats[0].rowNumber", is(1)))
                .andExpect(jsonPath("$.seats[0].seatNumber", is(4)))
                .andExpect(jsonPath("$.seats[0].reservationStatus", is(ReservationStatus.HELD.name())))
                .andExpect(jsonPath("$.seats[1].rowNumber", is(2)))
                .andExpect(jsonPath("$.seats[1].seatNumber", is(1)))
                .andExpect(jsonPath("$.seats[1].reservationStatus", is(ReservationStatus.HELD.name())))
                .andExpect(jsonPath("$.seats[2].rowNumber", is(2)))
                .andExpect(jsonPath("$.seats[2].seatNumber", is(2)))
                .andExpect(jsonPath("$.seats[2].reservationStatus", is(ReservationStatus.HELD.name())));

        //Below code tests whether tickets are held for specific period of time
        mockMvc.perform(get("/tickets/Venue1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", is(0)));

        //If this test fails, check whether TIMEOUTINSECONDS is same in TicketServiceImpl
        Thread.sleep(TIMEOUTINSECONDS * 1000);

        mockMvc.perform(get("/tickets/Venue1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", is(3)));

    }


    @Test
    public void createReservation() throws Exception{
        mockMvc.perform(post("/tickets/reserve/"+seatHolds.get(0).getSeatHoldId()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.venueNumber", is(seatHolds.get(0).getSeats().get(0).getRow().getVenue().getVenueNumber())))
                .andExpect(jsonPath("$.customerEmail", is(seatHolds.get(0).getCustomerEmail())))
                .andExpect(jsonPath("$.seats", hasSize(seatHolds.get(0).getSeats().size())))
                .andExpect(jsonPath("$.seats[0].rowNumber", is(seatHolds.get(0).getSeats().get(0).getRow().getRowNumber())))
                .andExpect(jsonPath("$.seats[0].seatNumber", is(seatHolds.get(0).getSeats().get(0).getSeatNumber())))
                .andExpect(jsonPath("$.seats[0].reservationStatus", is(ReservationStatus.RESERVED.name())))
                .andExpect(jsonPath("$.seats[1].rowNumber", is(seatHolds.get(0).getSeats().get(1).getRow().getRowNumber())))
                .andExpect(jsonPath("$.seats[1].seatNumber", is(seatHolds.get(0).getSeats().get(1).getSeatNumber())))
                .andExpect(jsonPath("$.seats[1].reservationStatus", is(ReservationStatus.RESERVED.name())))
                .andExpect(jsonPath("$.seats[2].rowNumber", is(seatHolds.get(0).getSeats().get(2).getRow().getRowNumber())))
                .andExpect(jsonPath("$.seats[2].seatNumber", is(seatHolds.get(0).getSeats().get(2).getSeatNumber())))
                .andExpect(jsonPath("$.seats[2].reservationStatus", is(ReservationStatus.RESERVED.name())));

        mockMvc.perform(get("/tickets/seatHold/"+seatHolds.get(0).getSeatHoldId()))
                .andExpect(status().isBadRequest());
    }

}
