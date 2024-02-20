package com.kbtg.bootcamp.posttest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kbtg.bootcamp.posttest.payload.response.TicketResponse;
import com.kbtg.bootcamp.posttest.payload.response.UserTicketIdResponse;
import com.kbtg.bootcamp.posttest.payload.response.UserTicketsResponse;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    MockMvc mockMvc;

    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Mock
    TicketService ticketService;

    @BeforeEach
    void setUp() {
        UserController userController = new UserController(ticketService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).alwaysDo(print()).build();
    }

    @Test
    @DisplayName("[buyLottery] Should be brought lottery (201)")
    void shouldBeBoughtLottery() throws Exception {

        // payload
        String userId = "1234567890";
        String ticketId = "123456";

        // response
        UserTicketIdResponse response = new UserTicketIdResponse(1);

        // Mock the behavior of the lotteryService.createUserTicket method
        when(ticketService.buyTicket(userId, ticketId))
                .thenReturn(response);

        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andReturn();
    }

    @Test
    @DisplayName("[buyLottery] Should be Bad request (400) with userId is less then 10 digits")
    void shouldBeBadRequestWithUserIdIsLessThanTenDigits() throws Exception {

        // payload
        String userId = "123456789";
        String ticketId = "123456";


        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[buyLottery] Should be Bad request (400) with userId is more then 10 digits")
    void shouldBeBadRequestWithUserIdIsMoreThanTenDigits() throws Exception {

        // payload
        String userId = "12345678901";
        String ticketId = "123456";


        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[buyLottery] Should be Bad request (400) with userId is not number")
    void shouldBeBadRequestWithUserIdIsNotNumber() throws Exception {

        // payload
        String userId = "ten";
        String ticketId = "000000";

        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[buyLottery] Should be Bad request (400) with ticketId is less then 6 digits")
    void shouldBeBadRequestWithTicketIdIsLessThanSixDigits() throws Exception {

        // payload
        String userId = "1234567890";
        String ticketId = "12345";

        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[buyLottery] Should be Bad request (400) with ticketId is more then 6 digits")
    void shouldBeBadRequestWithTicketIdIsMoreThanSixDigits() throws Exception {

        // payload
        String userId = "1234567890";
        String ticketId = "1234567";

        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[buyLottery] Should be Bad request (400) with ticketId is not number")
    void shouldBeBadRequestWithTicketIdIsNotNumber() throws Exception {

        // payload
        String userId = "1234567890";
        String ticketId = "OneTwoThreeFourFive";

        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[getUserLottery] Should be retrieve user's tickets, count and cost (200)")
    void shouldBeRetrieveUserTicketsCountAndCost() throws Exception {
        // payload
        String userId = "1234567890";
        List<String> tickets = new ArrayList<>();
        tickets.add("123456");
        tickets.add("222222");
        tickets.add("333333");

        int count = 3;
        int cost = 240;

        // response
        UserTicketsResponse response = new UserTicketsResponse(tickets, count, cost);

        // Mock the behavior of the lotteryService.findLotteries method
        when(ticketService.fetchUserTickets(userId))
                .thenReturn(response);

        mockMvc.perform(get("/users/" + userId + "/lotteries/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tickets", is(tickets)))
                .andExpect(jsonPath("$.count", is(3)))
                .andExpect(jsonPath("$.cost", is(240)))
                .andReturn();
    }

    @Test
    @DisplayName("[getUserLottery] Should be Bad Request (400) with userId is less than 10 digits")
    void shouldBeBadRequestWithUserIdIsLessThanTenDigitsAtGetUserLottery() throws Exception {
        // payload
        String userId = "123456789";

        mockMvc.perform(get("/users/" + userId + "/lotteries/"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[getUserLottery] Should be Bad Request (400) with userId is more than 10 digits")
    void shouldBeBadRequestWithUserIdIsMoreThanTenDigitsAtGetUserLottery() throws Exception {
        // payload
        String userId = "12345678901";

        mockMvc.perform(get("/users/" + userId + "/lotteries/"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[getUserLottery] Should be Bad Request (400) with userId is not number")
    void shouldBeBadRequestWithUserIdIsNotNumberAtGetUserLottery() throws Exception {
        // payload
        String userId = "OneTwoThreeFourFive";

        mockMvc.perform(get("/users/" + userId + "/lotteries/"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[deleteUserTicket] Should be delete user ticket (200)")
    void shouldBeDeleteUserTicket() throws Exception {
        // payload
        String userId = "1234567890";
        String ticket = "123456";

        // response
        TicketResponse response = new TicketResponse(ticket);

        // Mock the behavior of the lotteryService.findLotteries method
        when(ticketService.sellTicket(userId, ticket))
                .thenReturn(response);

        mockMvc.perform(delete("/users/" + userId + "/lotteries/" + ticket))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticket", is(ticket)))
                .andReturn();
    }

    @Test
    @DisplayName("[deleteUserTicket] Should be Bad Request (400) with userId is less than 10 digits")
    void shouldBeBadRequestWithUserIdIsLessThanTenDigitsAtDeleteUserTicket() throws Exception {
        // payload
        String userId = "123456789";
        String ticket = "123456";

        mockMvc.perform(delete("/users/" + userId + "/lotteries/" + ticket))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[deleteUserTicket] Should be Bad Request (400) with userId is More than 10 digits")
    void shouldBeBadRequestWithUserIdIsMoreThanTenDigitsAtDeleteUserTicket() throws Exception {
        // payload
        String userId = "12345678901";
        String ticket = "123456";

        mockMvc.perform(delete("/users/" + userId + "/lotteries/" + ticket))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[deleteUserTicket] Should be Bad Request (400) with userId is not number")
    void shouldBeBadRequestWithUserIdIsNotNumberAtDeleteUserTicket() throws Exception {
        // payload
        String userId = "OneTwoThreeFourFive";
        String ticket = "123456";

        mockMvc.perform(delete("/users/" + userId + "/lotteries/" + ticket))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[deleteUserTicket] Should be Bad Request (400) with ticketId is less than 6 digits")
    void shouldBeBadRequestWithTicketIdIsLessThanSixDigitsAtDeleteUserTicket() throws Exception {
        // payload
        String userId = "1234567890";
        String ticket = "12345";

        mockMvc.perform(delete("/users/" + userId + "/lotteries/" + ticket))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[deleteUserTicket] Should be Bad Request (400) with ticketId is more than 6 digits")
    void shouldBeBadRequestWithTicketIdIsMoreThanSixDigitsAtDeleteUserTicket() throws Exception {
        // payload
        String userId = "1234567890";
        String ticket = "1234567";

        mockMvc.perform(delete("/users/" + userId + "/lotteries/" + ticket))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("[deleteUserTicket] Should be Bad Request (400) with ticketId is not number")
    void shouldBeBadRequestWithTicketIdIsNotNumberAtDeleteUserTicket() throws Exception {
        // payload
        String userId = "1234567890";
        String ticket = "OneTwoThree";

        mockMvc.perform(delete("/users/" + userId + "/lotteries/" + ticket))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

}