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
    public TicketResponse registerLottery(LotteryRequest request) {

        // create new Lottery
        Lottery newLottery = new Lottery(request.ticket(), request.price(), request.amount());

        // save Lottery to database
        lotteryRepository.save(newLottery);

        return new TicketResponse(newLottery.getTicket());
    }

    @Override
    public TicketsResponse listAvailableLotteries() {

        // retrieve available lotteries in stock.
        List<Lottery> lotteries = lotteryRepository.findByAmountGreaterThanEqual(1);

        List<String> availableLotteries = new ArrayList<>();
        TicketsResponse response = new TicketsResponse();

        // response empty list
        if (lotteries.isEmpty()) {
            return response;
        }

        // create availableLotteries
        for (Lottery lottery : lotteries) {
            availableLotteries.add(lottery.getTicket());
        }

        // add ticket to response object
        response.setTickets(availableLotteries);

        return response;
    }

    @Transactional
    @Override
    public UserTicketIdResponse buyLotteryTicket(int userId, String tickerId) {

        // Find the existing availableLotteries in the database by ticket ID.
        List<Lottery> availableLotteries = lotteryRepository.findByTicketAndAmountGreaterThanEqual(tickerId, 1);

        if (availableLotteries.isEmpty()) {
            throw new StatusInternalServerErrorException(
                    "Lottery ticket number " + tickerId + " is not available or sold out already");
        }

        // get a lottery object
        Lottery selectedLottery = availableLotteries.get(0);

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

        // return empty information
        if (userTickets.isEmpty()) {
            return new UserLotteriesResponse();
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
        UserLotteriesResponse response = new UserLotteriesResponse();
        response.setTickets(tickets);
        response.setCount(count);
        response.setCost(price);

        return response;

    }

    @Override
    @Transactional
    public TicketResponse sellLotteryTicket(int userId, String tickerId) {

        // find lotteries from userId
        List<UserTicket> userTickets = userTicketRepository.findByUserId(userId);

        if (userTickets.isEmpty()) {
            throw new StatusInternalServerErrorException("User not found.");
        }

        // find A lottery that of input user.
        Optional<UserTicket> optionalUserTicket = userTickets.stream()
                .filter(tempLottery -> Objects.equals(tempLottery.getLottery().getTicket(), tickerId))
                .findFirst();

        if (optionalUserTicket.isEmpty()) {
            throw new StatusInternalServerErrorException("Ticket not found.");
        }

        // get A UserTicket
        UserTicket userTicket = optionalUserTicket.get();

        // get A Lottery from this Ticket
        Lottery lottery = userTicket.getLottery();

        // create the response
        TicketResponse response = new TicketResponse(lottery.getTicket());

        // remove UserTicket
        userTicketRepository.delete(userTicket);

        // update Amount to Lottery
        int newAmount = lottery.getAmount() + 1;
        lottery.setAmount(newAmount);
        lotteryRepository.save(lottery);

        return response;

    }
}
