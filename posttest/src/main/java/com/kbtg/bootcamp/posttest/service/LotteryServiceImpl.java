package com.kbtg.bootcamp.posttest.service;

import com.kbtg.bootcamp.posttest.entity.Lottery;
import com.kbtg.bootcamp.posttest.entity.UserTicket;
import com.kbtg.bootcamp.posttest.exception.StatusInternalServerErrorException;
import com.kbtg.bootcamp.posttest.payload.request.LotteryRequest;
import com.kbtg.bootcamp.posttest.payload.response.*;
import com.kbtg.bootcamp.posttest.repository.LotteryRepository;
import com.kbtg.bootcamp.posttest.repository.UserTicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public TicketResponse registerLottery(LotteryRequest request) {

        // check the existing Lottery
        String ticket = request.ticket();
        Optional<Lottery> tempLottery = Optional.ofNullable(lotteryRepository.findByTicket(ticket));

        // found the existing Lottery throw the exception status 400 Bad Request
        if (tempLottery.isPresent()) {
            throw new StatusInternalServerErrorException(
                    "This ticket already in the database. Ticket: " + ticket);
        }

        // create new Lottery
        Lottery lottery = new Lottery(request.ticket(), request.price(), request.amount());

        // save Lottery to database
        lotteryRepository.save(lottery);

        return new TicketResponse(lottery.getTicket());
    }

    @Override
    public TicketsResponse listAvailableLotteries() {

        // retrieve available lotteries in stock.
        List<Lottery> lotteries = lotteryRepository.findByAmountGreaterThanEqual(1);

        List<String> availableLotteries = new ArrayList<>();
        TicketsResponse ticketsResponse = new TicketsResponse();

        // response empty list
        if (lotteries.isEmpty()) {
            return ticketsResponse;
        }

        // create availableLotteries
        for (Lottery lottery : lotteries) {
            availableLotteries.add(lottery.getTicket());
        }

        // add ticket to response object
        ticketsResponse.setTickets(availableLotteries);

        // response ticket response
        return ticketsResponse;
    }

    @Transactional
    @Override
    public UserTicketIdResponse buyLotteryTicket(int userId, String tickerId) {

        // Find the existing optionLottery in the database by ticket ID.
        Optional<Lottery> optionLottery = Optional.ofNullable(
                lotteryRepository.findByTicketAndAmountGreaterThanEqual(tickerId, 1));
        if (optionLottery.isEmpty()) {
            throw new StatusInternalServerErrorException("Lottery ticket number " + tickerId + " is not available");
        }

        // get lottery object
        Lottery selectedLottery = optionLottery.get();

        // create UserTicket
        UserTicket userTicket = new UserTicket(userId, selectedLottery);

        // save UserTicket to database and receive id of user_ticket
        UserTicketIdResponse response = new UserTicketIdResponse();
        response.setId(userTicketRepository.save(userTicket).getId());

        // update amount to database
        int updateAmount = selectedLottery.getAmount() - 1;
        selectedLottery.setAmount(updateAmount);
        lotteryRepository.save(selectedLottery);

        return response;
    }

    @Override
    public UserLotteriesResponse fetchUserLotteries(int userId) {

        // find lottery by user id
        List<UserTicket> userTickets = userTicketRepository.findByUserId(userId);

        if (userTickets.isEmpty()) {
            throw new StatusInternalServerErrorException("Not found user id: " + userId);
        }

        // for collect tickets and calculate price and amount
        List<String> tickets = new ArrayList<>();
        int count = 0;
        int price = 0;

        for (UserTicket userTicket : userTickets) {
            tickets.add(userTicket.getLottery().getTicket());
            count += 1;
            price += userTicket.getLottery().getPrice();
        }

        // create UserLotteriesResponse
        UserLotteriesResponse userLotteriesResponse = new UserLotteriesResponse();
        userLotteriesResponse.setTickets(tickets);
        userLotteriesResponse.setCount(count);
        userLotteriesResponse.setCost(price);

        return userLotteriesResponse;

    }

    @Override
    @Transactional
    public TicketResponse sellLotteryTicket(int userId, String tickerId) {

        // find lottery by user id
        Optional<Lottery> tempLottery = Optional.ofNullable(lotteryRepository.findByTicket(tickerId));

        if (tempLottery.isEmpty()) {
            throw new StatusInternalServerErrorException("Lottery is not found with Ticket ID: " + tickerId);
        }

        // find lottery owner from userId
        Lottery lottery = tempLottery.get();
        Optional<UserTicket> userTicket = lottery.getUserTickets()
                .stream()
                .filter(user -> user.getUserId() == userId)
                .findFirst();

        if (userTicket.isEmpty()) {
            throw new StatusInternalServerErrorException("Lottery is not found with Ticket ID: " + tickerId);
        }

        // if it has lottery
        UserTicket soldUserTicket = userTicket.get();
        userTicketRepository.delete(soldUserTicket);

        return new TicketResponse(soldUserTicket.getLottery().getTicket());
    }
}
