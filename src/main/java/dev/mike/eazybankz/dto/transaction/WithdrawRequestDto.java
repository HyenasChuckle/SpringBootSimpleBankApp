package dev.mike.eazybankz.dto.transaction;

import java.math.BigDecimal;

public record WithdrawRequestDto(
        Long id,
        BigDecimal amount) {}
