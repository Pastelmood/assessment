package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.payload.request.LotteryRequest;
import com.kbtg.bootcamp.posttest.payload.response.*;

public interface LotteryService {

    TicketsResponse listAvailableLotteries();

    UserLotteriesResponse fetchUserLotteries(String userId);

    TicketResponse registerLottery(LotteryRequest lotteryRequest);

    UserTicketIdResponse buyLotteryTicket(String userId, String tickerId);

    TicketResponse sellLotteryTicket(String userId, String tickerId);

}
