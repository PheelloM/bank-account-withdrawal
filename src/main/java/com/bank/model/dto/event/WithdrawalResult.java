package com.bank.model.dto.event;

import java.math.BigDecimal;

public record WithdrawalResult(
        Long accountId,
        BigDecimal amount,
        String transactionId
) {
}
