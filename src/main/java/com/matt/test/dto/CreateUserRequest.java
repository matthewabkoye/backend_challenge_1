package com.matt.test.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.matt.test.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "deposit requires")
    private Double deposit;
    @NotNull(message = "role requires")
    private Role role;
}
