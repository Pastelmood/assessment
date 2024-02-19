package com.kbtg.bootcamp.posttest.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTicketsResponse {

    private List<String> tickets;

    private int count;

    private int cost;

}
