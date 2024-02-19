package com.kbtg.bootcamp.posttest.repository;

import com.kbtg.bootcamp.posttest.entity.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, Integer> {

    List<Lottery> findByTicket(String ticketId);

    List<Lottery> findByTicketAndAmountGreaterThanEqual(String ticketId, int amount);

    List<Lottery> findByAmountGreaterThanEqual(int amount);

}
