package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.payload.request.TicketRequest;
import com.kbtg.bootcamp.posttest.payload.response.TicketResponse;
import com.kbtg.bootcamp.posttest.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final TicketService ticketService;

    @Autowired
    public AdminController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/lotteries")
    public ResponseEntity<TicketResponse> createLottery(
            @Valid @RequestBody TicketRequest ticketRequest) {
        return new ResponseEntity<>(
                ticketService.registerTicket(ticketRequest), HttpStatus.CREATED);
    }
}
