package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.payload.response.TicketsResponse;
import com.kbtg.bootcamp.posttest.service.TicketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lotteries")
public class LotteryController {

    private TicketService ticketService;

    public LotteryController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("")
    public TicketsResponse getAllLotteries() {
        return ticketService.listAvailableTickets();
    }

}
