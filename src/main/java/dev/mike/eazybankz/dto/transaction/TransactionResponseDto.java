package dev.mike.eazybankz.dto.transaction;

import dev.mike.eazybankz.entity.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDto(
        TransactionType type,
        String sourceNumber,
        String receiverNumber,
        BigDecimal amount,
        LocalDateTime timestamp) {}
