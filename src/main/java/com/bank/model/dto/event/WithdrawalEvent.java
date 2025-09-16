package com.bank.model.dto.event;

import java.math.BigDecimal;

public record WithdrawalEvent(
        BigDecimal amount,
        Long accountId,
        String transactionId,
        String status
) {
    public String toJson() {
        return String.format("""
                {
                    "amount": "%s",
                    "accountId": %d,
                    "transactionId": "%s",
                    "status": "%s"
                }
                """, amount, accountId, transactionId, status);
    }
}
