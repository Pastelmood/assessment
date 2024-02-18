package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.payload.request.LotteryRequest;
import com.kbtg.bootcamp.posttest.payload.response.*;

public interface LotteryService {

    TicketsResponse findAllTickets();

    UserLotteriesResponse findLotteries(int userId);

    TicketResponse createLottery(LotteryRequest lotteryRequest);

    UserTicketIdResponse createUserTicket(int userId, String tickerId);

    TicketResponse deleteUserTicket(int userId, String tickerId);

}
