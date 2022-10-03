package com.matt.test.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserRequest {
    private String role;
    private Double deposit;
    private String password;
}
