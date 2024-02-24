package com.kbtg.bootcamp.posttest.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kbtg.bootcamp.posttest.payload.response.TicketsResponse;
import com.kbtg.bootcamp.posttest.service.TicketService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class LotteryControllerTest {

    MockMvc mockMvc;

    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Mock TicketService ticketService;

    @BeforeEach
    void setUp() {
        LotteryController lotteryController = new LotteryController(ticketService);
        mockMvc = MockMvcBuilders.standaloneSetup(lotteryController).alwaysDo(print()).build();
    }

    @Test
    @DisplayName("Should be retrieve tickets (200)")
    void shouldBeRetrieveTickets() throws Exception {

        List<String> tickets = new ArrayList<>();
        tickets.add("123456");
        tickets.add("654321");
        tickets.add("777777");

        TicketsResponse ticketsResponse = new TicketsResponse(tickets);
        String response = objectWriter.writeValueAsString(ticketsResponse);

        // Mock the behavior of the lotteryService.findAllTickets method
        when(ticketService.listAvailableTickets()).thenReturn(ticketsResponse);

        mockMvc.perform(get("/lotteries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tickets", is(tickets)))
                .andReturn();
    }
}
