package com.matt.test.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CreateUserResponse {
    @NotBlank(message = "username requires")
    private String username;
    @NotNull
    private Double deposit;
    @NotBlank(message = "role requires")
    @Size(max = 6, min =5, message = "Role can either be buyer of Seller")
    private String role;

    private Long id;
}
