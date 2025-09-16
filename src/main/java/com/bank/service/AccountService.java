package com.bank.service;

import com.bank.model.dto.event.WithdrawalResult;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidAmountException;
import com.bank.model.entity.Account;
import com.bank.model.entity.Transaction;
import com.bank.model.enums.TransactionStatus;
import com.bank.model.enums.TransactionType;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import com.bank.util.TransactionIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final WithdrawalEventPublisher eventPublisher;
    private final TransactionIdGenerator transactionIdGenerator;

    @Transactional
    public WithdrawalResult processWithdrawal(Long accountId, BigDecimal amount) {
        log.info("Processing withdrawal for account: {}, amount: {}", accountId, amount);

        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        validateWithdrawal(account, amount);

        String transactionId = transactionIdGenerator.generate();
        performWithdrawal(account, amount, transactionId);

        WithdrawalResult result = new WithdrawalResult(accountId, amount, transactionId);
        eventPublisher.publishWithdrawalEventAsync(result);

        return result;
    }

    private void validateWithdrawal(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(account.getId(), amount, account.getBalance());
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }
    }

    private void performWithdrawal(Account account, BigDecimal amount, String transactionId) {
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction(
                transactionId,
                account,
                amount,
                TransactionType.WITHDRAWAL,
                TransactionStatus.COMPLETED,
                null
        );
        transactionRepository.save(transaction);
    }
}
