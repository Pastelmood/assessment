package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.payload.request.TicketRequest;
import com.kbtg.bootcamp.posttest.payload.response.*;

public interface LotteryService {

    TicketsResponse listAvailableTickets();

    UserTicketsResponse fetchUserTickets(String userId);

    TicketResponse registerTicket(TicketRequest request);

    UserTicketIdResponse buyTicket(String userId, String tickerId);

    TicketResponse sellTicket(String userId, String tickerId);

}
