package com.kbtg.bootcamp.posttest.repository;

import com.kbtg.bootcamp.posttest.entity.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, Integer> {

    List<Lottery> findByTicket(String ticket);

    List<Lottery> findByTicketAndAmountGreaterThanEqual(String ticket, int amount);

    List<Lottery> findByAmountGreaterThanEqual(int amount);

}
