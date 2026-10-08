package dev.mike.eazybankz.dto.transaction;

import java.math.BigDecimal;

public record DepositRequestDto(String number, BigDecimal amount) implements TransactionRequestDtoInterface {

    @Override
    public String getSourceNumber() {
        return number;
    }

    @Override
    public String getReceiverNumber() {
        return number;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }
}
