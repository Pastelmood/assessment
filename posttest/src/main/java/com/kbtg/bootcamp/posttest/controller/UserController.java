package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.payload.response.TicketResponse;
import com.kbtg.bootcamp.posttest.payload.response.UserTicketIdResponse;
import com.kbtg.bootcamp.posttest.payload.response.UserTicketsResponse;
import com.kbtg.bootcamp.posttest.service.TicketService;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final TicketService ticketService;

    public UserController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/{userId}/lotteries/{ticketId}")
    public ResponseEntity<UserTicketIdResponse> buyLottery(
            @PathVariable(name = "userId")
            @NotBlank(message = "User ID value must not be blank")
            @Pattern(regexp = "\\d{10}", message = "User ID must be a 10-digit number")
            String userId,

            @PathVariable(name = "ticketId")
            @NotBlank
            @Pattern(regexp = "\\d{6}", message = "Value must be a 6-digit number")
            String ticketId
    ) {

        return new ResponseEntity<>(ticketService.buyTicket(userId, ticketId), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/lotteries")
    public UserTicketsResponse getUserLottery(
            @PathVariable(name = "userId")
            @NotBlank(message = "User ID value must not be blank")
            @Pattern(regexp = "\\d{10}", message = "User ID must be a 10-digit number")
            String userId
    ) {
        return ticketService.fetchUserTickets(userId);
    }

    @DeleteMapping("/{userId}/lotteries/{ticketId}")
    public TicketResponse deleteUserTicket(
            @PathVariable(name = "userId")
            @NotBlank(message = "User ID value must not be blank")
            @Pattern(regexp = "\\d{10}", message = "User ID must be a 10-digit number")
            String userId,

            @PathVariable(name = "ticketId")
            @NotBlank
            @Pattern(regexp = "\\d{6}", message = "Value must be a 6-digit number")
            String ticketId
    ) {

        return ticketService.sellTicket(userId, ticketId);

    }

}
