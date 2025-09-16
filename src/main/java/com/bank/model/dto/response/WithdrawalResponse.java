package com.bank.model.dto.response;

public record WithdrawalResponse(
        String transactionId,
        String message
) {
}
