package dev.mike.eazybankz.dto.transaction;

import java.math.BigDecimal;

public record DepositRequestDto(
        Long id,
        BigDecimal amount) {}
