package com.kbtg.bootcamp.posttest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kbtg.bootcamp.posttest.payload.response.TicketResponse;
import com.kbtg.bootcamp.posttest.payload.response.UserLotteriesResponse;
import com.kbtg.bootcamp.posttest.payload.response.UserTicketIdResponse;
import com.kbtg.bootcamp.posttest.service.LotteryService;
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
    LotteryService lotteryService;

    @BeforeEach
    void setUp() {
        UserController userController = new UserController(lotteryService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).alwaysDo(print()).build();
    }

    @Test
    @DisplayName("buyLottery with normal payload should be return 201 and has json path ... id")
    void buyLottery() throws Exception {

        // payload
        int userId = 1;
        String ticketId = "000000";

        // response
        UserTicketIdResponse response = new UserTicketIdResponse(1);

        // Mock the behavior of the lotteryService.createUserTicket method
        when(lotteryService.buyLotteryTicket(userId, ticketId))
                .thenReturn(response);

        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andReturn();
    }

    @Test
    @DisplayName("buyLottery with userId less than 1 should be return 400")
    void buyLotteryWithUserIdLessThanOneShouldBeReturnStatus400() throws Exception {

        // payload
        int userId = 0;
        String ticketId = "000000";


        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("buyLottery with userId is Ten should be return 400")
    void buyLotteryWithUserIdIsTenShouldBeReturnStatus400() throws Exception {

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
    @DisplayName("buyLottery with ticketId 5 digits should be return 400")
    void buyLotteryWithTicketIdFiveDigitsShouldBeReturnStatus400() throws Exception {

        // payload
        int userId = 1;
        String ticketId = "12345";

        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("buyLottery with ticketId 7 digits should be return 400")
    void buyLotteryWithTicketIdSevenDigitsShouldBeReturnStatus400() throws Exception {

        // payload
        int userId = 1;
        String ticketId = "1234567";

        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("buyLottery with ticketId is Character combine with number should be return 400")
    void buyLotteryWithTicketIdIsCharAndNumberShouldBeReturnStatus400() throws Exception {

        // payload
        int userId = 1;
        String ticketId = "123ABC";

        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("buyLottery with ticketId is none should be return 405")
    void buyLotteryWithTicketIdIsMissingShouldBeReturnStatus405() throws Exception {

        // payload
        int userId = 1;
        String ticketId = "";

        mockMvc.perform(post("/users/" + userId + "/lotteries/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }

    @Test
    @DisplayName("getUserLottery with normal payload should be return status 200 and jason path are correct")
    void getUserLotteryWithUserIdNormalShouldBeReturnStatus200() throws Exception {
        // payload
        int userId = 1;
        List<String> tickets = new ArrayList<>();
        tickets.add("123456");
        tickets.add("222222");
        tickets.add("333333");

        int count = 3;
        int cost = 240;

        // response
        UserLotteriesResponse response = new UserLotteriesResponse(tickets, count, cost);

        // Mock the behavior of the lotteryService.findLotteries method
        when(lotteryService.fetchUserLotteries(userId))
                .thenReturn(response);

        mockMvc.perform(get("/users/" + userId + "/lotteries/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tickets", is(tickets)))
                .andExpect(jsonPath("$.count", is(3)))
                .andExpect(jsonPath("$.cost", is(240)))
                .andReturn();
    }

    @Test
    @DisplayName("getUserLottery with userId less than 1 should be return status 400")
    void getUserLotteryWithUserIdLessThanOneShouldBeReturnStatus400() throws Exception {
        // payload
        int userId = 0;

        mockMvc.perform(get("/users/" + userId + "/lotteries/"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("getUserLottery with userId is string (One) should be return status 400")
    void getUserLotteryWithUserIdIsStringShouldBeReturnStatus400() throws Exception {
        // payload
        String userId = "One";

        mockMvc.perform(get("/users/" + userId + "/lotteries/"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("deleteUserTicket should be return status 200 and jsonPath ... ticket")
    void deleteUserTicketShouldBeReturnStatus200() throws Exception {
        // payload
        int userId = 1;
        String ticket = "000000";

        // response
        TicketResponse response = new TicketResponse(ticket);

        // Mock the behavior of the lotteryService.findLotteries method
        when(lotteryService.sellLotteryTicket(userId, ticket))
                .thenReturn(response);

        mockMvc.perform(delete("/users/" + userId + "/lotteries/" + ticket))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticket", is(ticket)))
                .andReturn();
    }

    @Test
    @DisplayName("deleteUserTicket with userId is less then 1 should be return status 400")
    void deleteUserTicketWithUserIdLessThanOneShouldBeReturnStatus400() throws Exception {
        // payload
        int userId = 0;
        String ticket = "000000";

        mockMvc.perform(delete("/users/" + userId + "/lotteries/" + ticket))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("deleteUserTicket with userId is String (Two) should be return status 400")
    void deleteUserTicketWithUserIdIsStringShouldBeReturnStatus400() throws Exception {
        // payload
        String userId = "Two";
        String ticket = "000000";

        mockMvc.perform(delete("/users/" + userId + "/lotteries/" + ticket))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

}