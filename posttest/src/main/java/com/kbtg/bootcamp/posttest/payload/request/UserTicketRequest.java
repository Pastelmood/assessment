package com.kbtg.bootcamp.posttest.payload.request;

import lombok.Data;

@Data
public class UserTicketRequest {

    private int userId;

    private String ticketId;
}
