package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.payload.request.TicketRequest;
import com.kbtg.bootcamp.posttest.payload.response.*;

public interface TicketService {

    TicketsResponse listAvailableTickets();

    UserTicketsResponse fetchUserTickets(String userId);

    TicketResponse registerTicket(TicketRequest request);

    UserTicketIdResponse buyTicket(String userId, String ticketId);

    TicketResponse sellTicket(String userId, String ticketId);
}
