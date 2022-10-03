package com.matt.test.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)

@Data
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "username requires")
    private String username;
    @NotBlank(message = "password requires")
    private String password;
    @NotNull
    private double deposit;
    @NotBlank(message = "role requires")
    @Size(max = 6, min =5, message = "Role can either be buyer of Seller")
    private String role;
}
