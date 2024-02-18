package com.kbtg.bootcamp.posttest.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketResponse {

    private String ticket;

    public TicketResponse(String ticket) {
        this.ticket = ticket;
    }

}
