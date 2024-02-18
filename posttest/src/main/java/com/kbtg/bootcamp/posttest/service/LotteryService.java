package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.payload.request.LotteryRequest;
import com.kbtg.bootcamp.posttest.payload.response.*;

public interface LotteryService {

    TicketsResponse listAvailableLotteries();

    UserLotteriesResponse fetchUserLotteries(int userId);

    TicketResponse registerLottery(LotteryRequest lotteryRequest);

    UserTicketIdResponse buyLotteryTicket(int userId, String tickerId);

    TicketResponse sellLotteryTicket(int userId, String tickerId);

}
