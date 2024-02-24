package com.kbtg.bootcamp.posttest.payload.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTicketsResponse {

    private List<String> tickets;

    private int count;

    private int cost;
}
