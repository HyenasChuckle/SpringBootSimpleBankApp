package dev.mike.eazybankz.dto.customer;

import jakarta.validation.constraints.NotBlank;

public record CustomerSignUpDto(
        @NotBlank(message = "first name is required")
        String firstName,

        @NotBlank(message = "last name is required")
        String lastName,

        @NotBlank(message = "username is required")
        String email,

        @NotBlank(message = "password is required")
        String password) {}
