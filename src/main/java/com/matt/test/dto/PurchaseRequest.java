package com.matt.test.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PurchaseRequest {
    @NotNull(message = "Quantity cannot be null")
    private int quantity;
    @NotNull(message = "ProductId cannot be null")
    private Long productId;
}
