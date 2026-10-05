package dev.mike.easybank.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterCustomerDTO(
        @NotBlank(message = "username is required")
        String username,

        @NotBlank(message = "password is required")
        String password) {}
