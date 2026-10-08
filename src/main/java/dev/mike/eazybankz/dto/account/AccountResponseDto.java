package dev.mike.eazybankz.dto.account;

import java.math.BigDecimal;

public record AccountResponseDto(String number, BigDecimal balance) {}
