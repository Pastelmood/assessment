package com.kbtg.bootcamp.posttest.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TicketsResponse {

    List<String> tickets;

    public TicketsResponse(List<String> tickets) {
        this.tickets = tickets;
    }

}
