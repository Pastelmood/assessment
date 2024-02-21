package com.kbtg.bootcamp.posttest.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTicketsResponse {

    private List<String> tickets;

    private int count;

    private int cost;

}
