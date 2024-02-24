package com.kbtg.bootcamp.posttest.repository;

import com.kbtg.bootcamp.posttest.entity.Ticket;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    Optional<Ticket> findByTicketId(String ticketId);

    Optional<Ticket> findByTicketIdAndAmountGreaterThanEqual(String ticketId, int amount);

    List<Ticket> findByAmountGreaterThanEqual(int amount);
}
