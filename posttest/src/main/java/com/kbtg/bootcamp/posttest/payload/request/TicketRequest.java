package com.kbtg.bootcamp.posttest.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketRequest {

    @NotBlank(message = "Ticket ID cannot be blank")
    @Pattern(regexp = "\\d{6}", message = "Value must be a 6-digit number")
    String ticket;

    @NotNull(message = "Price cannot be null") @Min(value = 1, message = "Price must be at least 1")
    int price;

    @NotNull(message = "Amount cannot be null") @Min(value = 1, message = "Amount must be at least 1")
    Integer amount;
}
