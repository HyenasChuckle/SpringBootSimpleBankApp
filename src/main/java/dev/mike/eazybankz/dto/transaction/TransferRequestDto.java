package dev.mike.eazybankz.dto.transaction;

import java.math.BigDecimal;

public record TransferRequestDto(String sourceNumber, String receiverNumber, BigDecimal amount)
        implements TransactionRequestDtoInterface {

    @Override
    public String getSourceNumber() {
        return sourceNumber;
    }

    @Override
    public String getReceiverNumber() {
        return receiverNumber;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }
}
