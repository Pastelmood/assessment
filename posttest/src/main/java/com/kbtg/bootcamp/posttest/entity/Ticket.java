package com.kbtg.bootcamp.posttest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity @Table(name = "lottery", schema = "public")
public class Ticket {

    @Id
    @Column(name = "ticket_id", nullable = false, length = 6)
    private String ticketId;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "amount", nullable = false)
    private int amount;

    @OneToMany(mappedBy = "ticket")
    private List<UserTicket> userTickets;

}
