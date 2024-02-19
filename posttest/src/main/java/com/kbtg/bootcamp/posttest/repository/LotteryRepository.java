package com.kbtg.bootcamp.posttest.repository;

import com.kbtg.bootcamp.posttest.entity.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotteryRepository extends JpaRepository<Ticket, String> {

    Ticket findByTicketId(String ticketId);

    Optional<Ticket> findByTicketIdAndAmountGreaterThanEqual(String ticketId, int amount);

    List<Ticket> findByAmountGreaterThanEqual(int amount);

}
