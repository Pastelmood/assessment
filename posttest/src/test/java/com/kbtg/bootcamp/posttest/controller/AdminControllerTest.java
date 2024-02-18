package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.payload.request.LotteryRequest;
import com.kbtg.bootcamp.posttest.payload.response.TicketResponse;
import com.kbtg.bootcamp.posttest.service.LotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    MockMvc mockMvc;

    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Mock
    LotteryService lotteryService;



    @BeforeEach
    void setUp() {
        AdminController adminController = new AdminController(lotteryService);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).alwaysDo(print()).build();
    }

    @Test
    @DisplayName("When create lottery should be return status 201 created and ticket value is 000000")
    void createLotteryShouldBeReturnStatusCreated() throws Exception {

        LotteryRequest lotteryRequest = new LotteryRequest("000000", 80, 1);
        String request = objectWriter.writeValueAsString(lotteryRequest);

        // Mock the behavior of the lotteryService.createLottery method
        TicketResponse response = new TicketResponse("000000");
        when(lotteryService.createLottery(any()))
                .thenReturn(response);

        // Perform the POST request
        mockMvc.perform(post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticket", is("000000")))
                .andReturn();
    }

    @Test
    @DisplayName("Create lottery with ticket AAA000 should be return status 400 bad request")
    void createLotteryWithLetterShouldBeReturnStatusBadRequest() throws Exception {
        LotteryRequest lotteryRequest = new LotteryRequest("AAA000", 80, 1);
        String request = objectWriter.writeValueAsString(lotteryRequest);

        // Perform the POST request
        mockMvc.perform(post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create lottery with ticket 12345 (less than 6 letters) should be return status 400 bad request")
    void createLotteryWithLessThan6LetterShouldBeReturnStatusBadRequest() throws Exception {
        LotteryRequest lotteryRequest = new LotteryRequest("12345", 80, 1);
        String request = objectWriter.writeValueAsString(lotteryRequest);

        // Perform the POST request
        mockMvc.perform(post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create lottery with price is 2K (not number) should be return status 400 bad request")
    void createLotteryWithNotNumberOfPriceShouldBeReturnStatusBadRequest() throws Exception {

        // Perform the POST request
        mockMvc.perform(post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"ticket\": \"000000\", \"price\": \"2K\", \"amount\": 1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create lottery with price is less than 0 should be return status 400 bad request")
    void createLotteryWithPriceLessThanZeroShouldBeReturnStatusBadRequest() throws Exception {

        // Perform the POST request
        mockMvc.perform(post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"ticket\": \"000000\", \"price\": -30, \"amount\": 1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create lottery with amount is less than 0 should be return status 400 bad request")
    void createLotteryWithAmountLessThanZeroShouldBeReturnStatusBadRequest() throws Exception {

        // Perform the POST request
        mockMvc.perform(post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"ticket\": \"000000\", \"price\": 80, \"amount\": -88}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create lottery with amount is not number should be return status 400 bad request")
    void createLotteryWithAmountIsNotNumberShouldBeReturnStatusBadRequest() throws Exception {

        // Perform the POST request
        mockMvc.perform(post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"ticket\": \"000000\", \"price\": 80, \"amount\": \"45ea\"}"))
                .andExpect(status().isBadRequest());
    }



}