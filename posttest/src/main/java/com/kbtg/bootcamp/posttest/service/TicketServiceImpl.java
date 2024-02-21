package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.entity.Ticket;
import com.kbtg.bootcamp.posttest.entity.UserTicket;
import com.kbtg.bootcamp.posttest.exception.StatusInternalServerErrorException;
import com.kbtg.bootcamp.posttest.payload.request.TicketRequest;
import com.kbtg.bootcamp.posttest.payload.response.*;
import com.kbtg.bootcamp.posttest.repository.TicketRepository;
import com.kbtg.bootcamp.posttest.repository.UserTicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserTicketRepository userTicketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, UserTicketRepository userTicketRepository) {
        this.ticketRepository = ticketRepository;
        this.userTicketRepository = userTicketRepository;
    }

    @Override
    @Transactional
    public TicketResponse registerTicket(TicketRequest request) {

        // find an existing ticket and throw an exception if one is found.
        Optional<Ticket> optionalTicket = ticketRepository.findByTicketId(request.getTicket());
        if (optionalTicket.isPresent()) {
            throw new StatusInternalServerErrorException("Ticket ID " + request.getTicket() + " already in the store");
        }

        // create new Lottery object
        Ticket ticket = Ticket.builder()
                .ticketId(request.getTicket())
                .price(request.getPrice())
                .amount(request.getAmount())
                .build();

        // save Lottery to database
        ticketRepository.save(ticket);

        return new TicketResponse(ticket.getTicketId());
    }

    @Override
    public TicketsResponse listAvailableTickets() {

        // retrieve available tickets in stock.
        List<Ticket> tickets = ticketRepository.findByAmountGreaterThanEqual(1);

        List<String> availableTickets = new ArrayList<>();
        TicketsResponse response = new TicketsResponse();

        // response empty list
        if (tickets.isEmpty()) {
            return response;
        }

        // create availableLotteries
        for (Ticket ticket : tickets) {
            availableTickets.add(ticket.getTicketId());
        }

        // add ticket to response object
        response.setTickets(availableTickets);

        return response;
    }

    @Transactional
    @Override
    public UserTicketIdResponse buyTicket(String userId, String ticketId) {

        // Check whether this ticket is available in the store or not.
        // find a ticket from ticketId with an amount greater than 0
        Optional<Ticket> optionalTicket = ticketRepository.findByTicketIdAndAmountGreaterThanEqual(ticketId, 1);

        if (optionalTicket.isEmpty()) {
            throw new StatusInternalServerErrorException(
                    "Ticket number " + ticketId + " is not available or sold out already");
        }

        // get a selectedTicket object
        Ticket selectedTicket = optionalTicket.get();

        // create UserTicket
        UserTicket userTicket = UserTicket.builder().userId(userId).ticket(selectedTicket).build();

        // save UserTicket to database and receive id of user_ticket
        UserTicket savedUserTicket = userTicketRepository.save(userTicket);

        // update amount to database
        int updateAmount = selectedTicket.getAmount() - 1;
        selectedTicket.setAmount(updateAmount);
        ticketRepository.save(selectedTicket);

        // return id of table user_ticket
        return UserTicketIdResponse.builder().id(savedUserTicket.getId()).build();
    }

    @Override
    public UserTicketsResponse fetchUserTickets(String userId) {

        // find lottery by user id
        List<UserTicket> userTickets = userTicketRepository.findByUserId(userId);

        // return empty information
        if (userTickets.isEmpty()) {
            return new UserTicketsResponse();
        }

        // for collect tickets and calculate cost and amount
        List<String> tickets = new ArrayList<>();
        int count = 0;
        int cost = 0;

        for (UserTicket userTicket : userTickets) {
            tickets.add(userTicket.getTicket().getTicketId());
            count += 1;
            cost += userTicket.getTicket().getPrice();
        }

        // create UserLotteriesResponse
        return UserTicketsResponse.builder()
                .tickets(tickets)
                .count(count)
                .cost(cost)
                .build();

    }

    @Override
    @Transactional
    public TicketResponse sellTicket(String userId, String tickerId) {

        // find lotteries from userId
        List<UserTicket> userTickets = userTicketRepository.findByUserId(userId);

        if (userTickets.isEmpty()) {
            throw new StatusInternalServerErrorException("User not found.");
        }

        // find A lottery that of input user.
        Optional<UserTicket> optionalUserTicket = userTickets.stream()
                .filter(tempTicket -> Objects.equals(tempTicket.getTicket().getTicketId(), tickerId))
                .findFirst();

        if (optionalUserTicket.isEmpty()) {
            throw new StatusInternalServerErrorException("Ticket not found.");
        }

        // get A UserTicket
        UserTicket userTicket = optionalUserTicket.get();

        // get A Lottery from this Ticket
        Ticket ticket = userTicket.getTicket();

        // remove UserTicket
        userTicketRepository.delete(userTicket);

        // update Amount to Lottery
        int newAmount = ticket.getAmount() + 1;
        ticket.setAmount(newAmount);
        ticketRepository.save(ticket);

        // response sold ticket id
        return TicketResponse.builder().ticket(ticket.getTicketId()).build();

    }
}
