package dev.mike.easybank.dto.customer;

import jakarta.validation.constraints.NotBlank;

public record CustomerSignUpDTO(
        @NotBlank(message = "username is required")
        String username,

        @NotBlank(message = "password is required")
        String password) {}
