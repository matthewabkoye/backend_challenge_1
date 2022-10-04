package com.matt.test.dto;

import com.matt.test.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CreateUserResponse {
    @NotBlank(message = "username required")
    private String username;
    @NotNull(message = "Deposit required")
    private Double deposit;
    @NotNull(message = "role required")
    private Role role;
    private Long id;
}
