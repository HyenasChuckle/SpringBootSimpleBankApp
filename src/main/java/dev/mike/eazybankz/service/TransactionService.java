package dev.mike.eazybankz.service;

import dev.mike.eazybankz.dto.transaction.DepositRequestDto;
import dev.mike.eazybankz.dto.transaction.TransactionResponseDto;
import dev.mike.eazybankz.dto.transaction.TransferRequestDto;
import dev.mike.eazybankz.dto.transaction.WithdrawRequestDto;
import dev.mike.eazybankz.entity.Account;
import dev.mike.eazybankz.entity.Transaction;
import dev.mike.eazybankz.entity.enums.TransactionType;
import dev.mike.eazybankz.repository.AccountRepository;
import dev.mike.eazybankz.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransactionResponseDto createDeposit(DepositRequestDto dto, String email) {
        Account account = prepareAccount(dto.id(), email);

        Transaction transaction = Transaction
                .builder()
                .receiverIBAN(account.getIBAN())
                .transactionAmount(dto.amount())
                .sourceAccount(account)
                .transactionType(TransactionType.DEPOSIT)
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        account.setCurrentBalance(account.getCurrentBalance().add(dto.amount()));

        return toTransactionResponseDto(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponseDto createWithdraw(WithdrawRequestDto dto, String email) {
        Account account = prepareAccount(dto.id(), email);

        if (account.getCurrentBalance().compareTo(dto.amount()) < 0)
            throw new RuntimeException("Insufficient funds!");

        Transaction transaction = Transaction
                .builder()
                .receiverIBAN(account.getIBAN())
                .sourceAccount(account)
                .transactionAmount(dto.amount())
                .transactionType(TransactionType.WITHDRAW)
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        BigDecimal withdrawAmount = dto.amount();

        // Adjusting after-withdraw account balance.
        BigDecimal balanceBeforeWithdraw = account.getCurrentBalance();
        BigDecimal balanceAfterWithdraw = balanceBeforeWithdraw.subtract(withdrawAmount);
        account.setCurrentBalance(balanceAfterWithdraw);

        return toTransactionResponseDto(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponseDto createTransfer(TransferRequestDto dto, String email) {
        Account sourceAccount = prepareAccount(dto.id(), email);

        Account receiverAccount = accountRepository.findByIBAN(dto.receiverAccountNumber()).orElseThrow(
                () -> new RuntimeException("Receiver account not found!")
        );

        if (sourceAccount.getCurrentBalance().compareTo(dto.amount()) < 0)
            throw new RuntimeException("Insufficient funds!");

        Transaction transaction = Transaction
                .builder()
                .receiverIBAN(dto.receiverAccountNumber())
                .sourceAccount(sourceAccount)
                .transactionAmount(dto.amount())
                .transactionType(TransactionType.TRANSFER)
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        BigDecimal transferAmount = dto.amount();

        // Adjusting after-transfer source account balance.
        BigDecimal sourceAccountBalanceBeforeTransfer = sourceAccount.getCurrentBalance();
        BigDecimal sourceAccountBalanceAfterTransfer = sourceAccountBalanceBeforeTransfer.subtract(transferAmount);
        sourceAccount.setCurrentBalance(sourceAccountBalanceAfterTransfer);

        // Adjusting after-transfer receiver account balance.
        BigDecimal receiverAccountBalanceBeforeTransfer = receiverAccount.getCurrentBalance();
        BigDecimal receiverAccountBalanceAfterTransfer = receiverAccountBalanceBeforeTransfer.add(transferAmount);
        receiverAccount.setCurrentBalance(receiverAccountBalanceAfterTransfer);

        return toTransactionResponseDto(transactionRepository.save(transaction));
    }

    // Mapping transaction to response.
    private TransactionResponseDto toTransactionResponseDto(Transaction transaction) {

        return new TransactionResponseDto(
                transaction.getTransactionType(),
                transaction.getSourceAccount().getIBAN(),
                transaction.getReceiverIBAN(),
                transaction.getTransactionAmount(),
                transaction.getTimestamp()
        );
    }

    // Checking whether account exists and customer is the owner.
    private Account prepareAccount(long id, String email) {

        return accountRepository.findByIdAndAccountOwnerEmail(id, email).orElseThrow(
                () -> new RuntimeException("Account not found!")
        );
    }
}
