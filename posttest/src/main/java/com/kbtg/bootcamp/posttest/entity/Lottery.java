package com.kbtg.bootcamp.posttest.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Entity @Table(name = "lottery", schema = "public")
public class Lottery {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ticket")
    private String ticket;

    @Column(name = "price")
    private int price;

    @Column(name = "amount")
    private int amount;

    @OneToMany(mappedBy = "lottery")
    private List<UserTicket> userTickets;

    public Lottery(String ticket, int price, int amount) {
        this.ticket = ticket;
        this.price = price;
        this.amount = amount;
    }
}
