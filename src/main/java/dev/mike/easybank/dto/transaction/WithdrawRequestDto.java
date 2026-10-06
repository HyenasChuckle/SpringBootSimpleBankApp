package dev.mike.easybank.dto.transaction;

import java.math.BigDecimal;

public record WithdrawRequestDto(
        Long id,
        BigDecimal amount) {}
