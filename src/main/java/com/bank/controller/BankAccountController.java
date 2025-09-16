package com.bank.controller;

import com.bank.model.dto.event.WithdrawalResult;
import com.bank.model.dto.request.WithdrawalRequest;
import com.bank.model.dto.response.ApiResponse;
import com.bank.model.dto.response.WithdrawalResponse;
import com.bank.service.AccountService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Validated
public class BankAccountController {

    private final AccountService accountService;

    @PostMapping("/{accountId}/withdrawals")
    public ResponseEntity<ApiResponse<WithdrawalResponse>> withdraw(
            @PathVariable @NotNull Long accountId,
            @Valid @RequestBody WithdrawalRequest request) {


        WithdrawalResult result = accountService.processWithdrawal(accountId, request.amount());

        return ResponseEntity.ok(ApiResponse.success(
                new WithdrawalResponse(result.transactionId(), "Withdrawal processed successfully")
        ));
    }
}

