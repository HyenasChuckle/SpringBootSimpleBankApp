package dev.mike.eazybankz.dto.transaction;

import dev.mike.eazybankz.entity.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDto(
        TransactionType transactionType,
        String sourceIBAN,
        String receiverIBAN,
        BigDecimal transactionAmount,
        LocalDateTime timestamp) {}
