package com.matt.test.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateProductRequest {
    @NotBlank(message = "Product name required")
    private String productName;
    @Size(min = 5, message = "Minimum cost should be 5")
    @NotNull(message = "Cost required")
    private Double cost;
    @NotNull(message = "AmountAvailable required")
    private Integer amountAvailable;
}
