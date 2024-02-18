package com.kbtg.bootcamp.posttest.payload.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record LotteryRequest(

        @NotNull
        @Pattern(regexp = "\\d{6}", message = "Value must be a 6-digit number")
        String ticket,

        @NotNull(message = "Price must be at least 0.1")
        @DecimalMin(value = "0.01", message = "Price must be greater than or equal to 0.01")
        @DecimalMax(value = "9999.99", message = "Price must be less than or equal to 9999.99")
        int price,

        @NotNull(message = "Amount must be at least 1")
        @Min(value = 1, message = "Amount must be at least 1")
        @Max(value = 100, message = "Amount must be at most 100")
        Integer amount

) {}
