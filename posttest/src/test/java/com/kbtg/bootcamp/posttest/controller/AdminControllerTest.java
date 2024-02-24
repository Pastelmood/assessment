package com.kbtg.bootcamp.posttest.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kbtg.bootcamp.posttest.payload.request.TicketRequest;
import com.kbtg.bootcamp.posttest.payload.response.TicketResponse;
import com.kbtg.bootcamp.posttest.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    MockMvc mockMvc;

    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Mock TicketService ticketService;

    @BeforeEach
    void setUp() {
        AdminController adminController = new AdminController(ticketService);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).alwaysDo(print()).build();
    }

    @Test
    @DisplayName("Should be create lottery (201)")
    void shouldBeCreateLottery() throws Exception {

        TicketRequest ticketRequest = new TicketRequest("000000", 80, 1);
        String request = objectWriter.writeValueAsString(ticketRequest);

        // Mock the behavior of the lotteryService.createLottery method
        TicketResponse response = new TicketResponse("000000");
        when(ticketService.registerTicket(any())).thenReturn(response);

        // Perform the POST request
        mockMvc.perform(
                        post("/admin/lotteries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticket", is("000000")))
                .andReturn();
    }

    @Test
    @DisplayName("Should be Bad request (400) with ticket id is not number")
    void shouldBeBadRequestWithTicketIdIsNotNumber() throws Exception {

        TicketRequest ticketRequest = new TicketRequest("AAA000", 80, 1);
        String request = objectWriter.writeValueAsString(ticketRequest);

        // Perform the POST request
        mockMvc.perform(
                        post("/admin/lotteries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should be Bad request (400) with Ticket Id less than 6 digits")
    void shouldBeBadRequestWithTicketIdIsLessThanSixDigits() throws Exception {

        TicketRequest ticketRequest = new TicketRequest("12345", 80, 1);
        String request = objectWriter.writeValueAsString(ticketRequest);

        // Perform the POST request
        mockMvc.perform(
                        post("/admin/lotteries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should be Bad request (400) with price is not number")
    void shouldBeBadRequestWithPriceIsNotNumber() throws Exception {

        // Perform the POST request
        mockMvc.perform(
                        post("/admin/lotteries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"ticket\": \"000000\", \"price\": \"eighty\", \"amount\": 1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should be Bad Request (400) with price less than 0")
    void shouldBeBadRequestWithPriceIsLessThanZero() throws Exception {

        // Perform the POST request
        mockMvc.perform(
                        post("/admin/lotteries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"ticket\": \"000000\", \"price\": -30, \"amount\": 1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should be Bad Request (400) with amount is less than 0")
    void shouldBeBadRequestWithAmountIsLessThanZero() throws Exception {

        // Perform the POST request
        mockMvc.perform(
                        post("/admin/lotteries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"ticket\": \"000000\", \"price\": 80, \"amount\": -88}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should be Bad Request (400) with amount is not number")
    void shouldBeBadRequestWithAmountIsNotNumber() throws Exception {

        // Perform the POST request
        mockMvc.perform(
                        post("/admin/lotteries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"ticket\": \"000000\", \"price\": 80, \"amount\": \"45ea\"}"))
                .andExpect(status().isBadRequest());
    }
}
