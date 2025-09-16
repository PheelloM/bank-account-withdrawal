package com.bank.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    private final Long accountId;
    private final BigDecimal requestedAmount;
    private final BigDecimal currentBalance;

    public InsufficientFundsException(Long accountId, BigDecimal requestedAmount, BigDecimal currentBalance) {
        super(String.format("Insufficient funds. Account: %d, Requested: %s, Available: %s",
                accountId, requestedAmount, currentBalance));
        this.accountId = accountId;
        this.requestedAmount = requestedAmount;
        this.currentBalance = currentBalance;
    }

    // Getters
    public Long getAccountId() {
        return accountId;
    }
}
