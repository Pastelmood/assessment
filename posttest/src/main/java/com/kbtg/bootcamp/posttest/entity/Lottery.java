package com.kbtg.bootcamp.posttest.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity @Table(name = "lottery", schema = "public")
public class Lottery {

    @Id
    @Column(name = "ticket_id")
    private String ticketId;

    @Column(name = "price")
    private int price;

    @Column(name = "amount")
    private int amount;

    @OneToMany(mappedBy = "lottery")
    private List<UserTicket> userTickets;

    public Lottery(String ticketId, int price, int amount) {
        this.ticketId = ticketId;
        this.price = price;
        this.amount = amount;
    }
}
