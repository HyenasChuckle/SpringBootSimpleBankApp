package dev.mike.easybank.service;

import dev.mike.easybank.dto.transaction.DepositRequestDto;
import dev.mike.easybank.dto.transaction.TransactionResponseDto;
import dev.mike.easybank.dto.transaction.WithdrawRequestDto;
import dev.mike.easybank.entity.Account;
import dev.mike.easybank.entity.Transaction;
import dev.mike.easybank.entity.enums.TransactionType;
import dev.mike.easybank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;

    @Transactional
    public TransactionResponseDto createDeposit(DepositRequestDto depositRequestDto, String username) {
        Account account = accountRepository.findByIdAndOwnerUsername(depositRequestDto.id(), username).orElseThrow(
                () -> new RuntimeException("Account not found!")
        );

        Transaction transaction = Transaction
                .builder()
                .receiverAccountNumber(account.getNumber())
                .amount(depositRequestDto.amount())
                .senderAccount(account)
                .transactionType(TransactionType.DEPOSIT)
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        account.setBalance(account.getBalance().add(depositRequestDto.amount()));

        account.addTransaction(transaction);

        return toTransactionResponseDto(transaction);
    }

    @Transactional
    public TransactionResponseDto createWithdraw(WithdrawRequestDto withdrawRequestDto, String username) {
        Account account = accountRepository.findByIdAndOwnerUsername(withdrawRequestDto.id(), username).orElseThrow(
                () -> new RuntimeException("Account not found!")
        );

        if (account.getBalance().compareTo(withdrawRequestDto.amount()) < 0)
            throw new RuntimeException("Insufficient funds!");

        Transaction transaction = Transaction
                .builder()
                .receiverAccountNumber(account.getNumber())
                .amount(withdrawRequestDto.amount())
                .senderAccount(account)
                .transactionType(TransactionType.WITHDRAW)
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        account.setBalance(account.getBalance().subtract(withdrawRequestDto.amount()));

        account.addTransaction(transaction);

        return toTransactionResponseDto(transaction);
    }

    private TransactionResponseDto toTransactionResponseDto(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getTransactionType(),
                transaction.getSenderAccount().getNumber(),
                transaction.getReceiverAccountNumber(),
                transaction.getAmount(),
                transaction.getTimestamp()
        );
    }
}
