package com.matt.test.dto;

import com.matt.test.enums.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserRequest {
    private Role role;
    private Double deposit;
    private String password;
}
