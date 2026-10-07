package dev.mike.eazybankz.dto.customer;

public record CustomerUpdateDTO(
        String firstName,
        String lastName,
        String email,
        String password) {}


