package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.payload.response.TicketResponse;
import com.kbtg.bootcamp.posttest.payload.response.UserLotteriesResponse;
import com.kbtg.bootcamp.posttest.payload.response.UserTicketIdResponse;
import com.kbtg.bootcamp.posttest.service.LotteryService;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final LotteryService lotteryService;

    public UserController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @PostMapping("/{userId}/lotteries/{ticketId}")
    public ResponseEntity<UserTicketIdResponse> buyLottery(
            @PathVariable(name = "userId")
            @NotNull(message = "userId value must not be null")
            @Min(1)
            @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "userId must be an integer")
            int userId,

            @PathVariable(name = "ticketId")
            @NotNull
            @Pattern(regexp = "\\d{6}", message = "Value must be a 6-digit number")
            String ticketId
    ) {

        UserTicketIdResponse response = lotteryService.createUserTicket(userId, ticketId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/lotteries")
    public UserLotteriesResponse getUserLottery(
            @PathVariable("userId")
            @NotNull(message = "userId value must not be null")
            @Min(1)
            @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "userId must be an integer")
            int userId){
        return lotteryService.findLotteries(userId);
    }


    @DeleteMapping("/{userId}/lotteries/{ticketId}")
    public TicketResponse deleteUserTicket(
            @PathVariable(name = "userId")
            @NotNull(message = "userId value must not be null")
            @Min(1)
            @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "userId must be an integer")
            int userId,

            @PathVariable(name = "ticketId")
            @NotNull
            @Pattern(regexp = "\\d{6}", message = "Value must be a 6-digit number")
            String ticketId
    ){

        return lotteryService.deleteUserTicket(userId, ticketId);

    }

}
