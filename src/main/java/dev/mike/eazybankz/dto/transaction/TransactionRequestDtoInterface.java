package dev.mike.eazybankz.dto.transaction;

import java.math.BigDecimal;

public interface TransactionRequestDtoInterface {

    String getSourceNumber();

    String getReceiverNumber();

    BigDecimal getAmount();
}
