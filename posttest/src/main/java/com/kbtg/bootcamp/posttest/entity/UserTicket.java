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
    private Lottery lottery;

    public UserTicket(String userId, Lottery lottery) {
        this.userId = userId;
        this.lottery = lottery;
    }
}
