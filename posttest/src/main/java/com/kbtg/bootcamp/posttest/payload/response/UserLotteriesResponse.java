package com.kbtg.bootcamp.posttest.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLotteriesResponse {

    private List<String> tickets;

    private int count;

    private int cost;

}
