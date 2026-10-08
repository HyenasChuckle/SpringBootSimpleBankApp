package dev.mike.eazybankz.dto.customer;

public record CustomerUpdateDto(
        String firstName,
        String lastName,
        String email,
        String password) {}
