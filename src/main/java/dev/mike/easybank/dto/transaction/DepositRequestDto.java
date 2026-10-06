package dev.mike.easybank.dto.transaction;

import java.math.BigDecimal;

public record DepositRequestDto(
        Long id,
        BigDecimal amount) {}
