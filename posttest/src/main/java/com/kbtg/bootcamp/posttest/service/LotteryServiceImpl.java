package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.entity.Ticket;
import com.kbtg.bootcamp.posttest.entity.UserTicket;
import com.kbtg.bootcamp.posttest.exception.StatusInternalServerErrorException;
import com.kbtg.bootcamp.posttest.payload.request.TicketRequest;
import com.kbtg.bootcamp.posttest.payload.response.*;
import com.kbtg.bootcamp.posttest.repository.LotteryRepository;
import com.kbtg.bootcamp.posttest.repository.UserTicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LotteryServiceImpl implements LotteryService {

    private final LotteryRepository lotteryRepository;
    private final UserTicketRepository userTicketRepository;

    public LotteryServiceImpl(LotteryRepository lotteryRepository, UserTicketRepository userTicketRepository) {
        this.lotteryRepository = lotteryRepository;
        this.userTicketRepository = userTicketRepository;
    }

    @Override
    @Transactional
    public TicketResponse registerTicket(TicketRequest request) {

        // find an existing ticket and throw an exception if one is found.
        Optional<Ticket> optionalTicket = Optional.ofNullable(lotteryRepository.findByTicketId(request.ticket()));
        if (optionalTicket.isPresent()) {
            throw new StatusInternalServerErrorException("Ticket ID " + request.ticket() + " already in the store");
        }

        // create new Lottery object
        Ticket ticket = new Ticket(request.ticket(), request.price(), request.amount());

        // save Lottery to database
        lotteryRepository.save(ticket);

        return new TicketResponse(ticket.getTicketId());
    }

    @Override
    public TicketsResponse listAvailableTickets() {

        // retrieve available tickets in stock.
        List<Ticket> tickets = lotteryRepository.findByAmountGreaterThanEqual(1);

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
        Optional<Ticket> optionalTicket = lotteryRepository.findByTicketIdAndAmountGreaterThanEqual(ticketId, 1);

        if (optionalTicket.isEmpty()) {
            throw new StatusInternalServerErrorException(
                    "Lottery ticket number " + ticketId + " is not available or sold out already");
        }

        // get a selectedTicket object
        Ticket selectedTicket = optionalTicket.get();

        // create UserTicket
        UserTicket userTicket = new UserTicket(userId, selectedTicket);

        // save UserTicket to database and receive id of user_ticket
        UserTicketIdResponse response = new UserTicketIdResponse();
        response.setId(userTicketRepository.save(userTicket).getId());

        // update amount to database
        int updateAmount = selectedTicket.getAmount() - 1;
        selectedTicket.setAmount(updateAmount);
        lotteryRepository.save(selectedTicket);

        return response;
    }

    @Override
    public UserTicketsResponse fetchUserTickets(String userId) {

        // find lottery by user id
        List<UserTicket> userTickets = userTicketRepository.findByUserId(userId);

        // return empty information
        if (userTickets.isEmpty()) {
            return new UserTicketsResponse();
        }

        // for collect tickets and calculate price and amount
        List<String> tickets = new ArrayList<>();
        int count = 0;
        int price = 0;

        for (UserTicket userTicket : userTickets) {
            tickets.add(userTicket.getTicket().getTicketId());
            count += 1;
            price += userTicket.getTicket().getPrice();
        }

        // create UserLotteriesResponse
        UserTicketsResponse response = new UserTicketsResponse();
        response.setTickets(tickets);
        response.setCount(count);
        response.setCost(price);

        return response;

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
                .filter(tempLottery -> Objects.equals(tempLottery.getTicket().getTicketId(), tickerId))
                .findFirst();

        if (optionalUserTicket.isEmpty()) {
            throw new StatusInternalServerErrorException("Ticket not found.");
        }

        // get A UserTicket
        UserTicket userTicket = optionalUserTicket.get();

        // get A Lottery from this Ticket
        Ticket lottery = userTicket.getTicket();

        // create the response
        TicketResponse response = new TicketResponse(lottery.getTicketId());

        // remove UserTicket
        userTicketRepository.delete(userTicket);

        // update Amount to Lottery
        int newAmount = lottery.getAmount() + 1;
        lottery.setAmount(newAmount);
        lotteryRepository.save(lottery);

        return response;

    }
}
