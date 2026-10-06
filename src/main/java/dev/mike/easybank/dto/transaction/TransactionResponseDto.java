package dev.mike.easybank.dto.transaction;

import dev.mike.easybank.entity.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDto(
        TransactionType transactionType,
        String senderIban,
        String receiverIban,
        BigDecimal amount,
        LocalDateTime timestamp) {}
