package com.kbtg.bootcamp.posttest.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity @Table(name = "user_ticket", schema = "public")
public class UserTicket {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public UserTicket(String userId, Ticket ticket) {
        this.userId = userId;
        this.ticket = ticket;
    }
}
