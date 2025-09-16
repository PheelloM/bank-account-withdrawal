package com.bank.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


import java.math.BigDecimal;

// DTOs
public record WithdrawalRequest(
        @NotNull @Positive BigDecimal amount
) {
}
