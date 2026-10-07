package dev.mike.eazybankz.dto.transaction;

import java.math.BigDecimal;

public record TransferRequestDto(
        Long id,
        String receiverAccountNumber,
        BigDecimal amount) {}
