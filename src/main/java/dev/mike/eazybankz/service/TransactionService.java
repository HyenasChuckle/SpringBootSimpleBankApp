package dev.mike.eazybankz.service;

import dev.mike.eazybankz.dto.transaction.*;
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
        Account account = prepareAccount(dto.number(), email);
        Transaction transaction = performTransaction(dto, account);

        // Adjusting after-deposit account balance.
        account.setBalance(account.getBalance().add(dto.amount()));

        return toTransactionResponseDto(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponseDto createWithdraw(WithdrawRequestDto dto, String email) {
        Account account = prepareAccount(dto.number(), email);
        Transaction transaction = performTransaction(dto, account);

        // Adjusting after-withdraw account balance.
        BigDecimal balanceBeforeWithdraw = account.getBalance();
        BigDecimal balanceAfterWithdraw = balanceBeforeWithdraw.subtract(dto.amount());
        account.setBalance(balanceAfterWithdraw);

        return toTransactionResponseDto(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponseDto createTransfer(TransferRequestDto dto, String email) {
        Account receiverAccount = accountRepository.findByNumber(dto.receiverNumber()).orElseThrow(
                () -> new RuntimeException("Receiver account not found!")
        );

        Account sourceAccount = prepareAccount(dto.getSourceNumber(), email);
        Transaction transaction = performTransaction(dto, sourceAccount);

        BigDecimal amount = dto.amount();

        // Adjusting after-transfer source account balance.
        BigDecimal sourceAccountBalanceBeforeTransfer = sourceAccount.getBalance();
        BigDecimal sourceAccountBalanceAfterTransfer = sourceAccountBalanceBeforeTransfer.subtract(amount);
        sourceAccount.setBalance(sourceAccountBalanceAfterTransfer);

        // Adjusting after-transfer receiver account balance.
        BigDecimal receiverAccountBalanceBeforeTransfer = receiverAccount.getBalance();
        BigDecimal receiverAccountBalanceAfterTransfer = receiverAccountBalanceBeforeTransfer.add(amount);
        receiverAccount.setBalance(receiverAccountBalanceAfterTransfer);

        return toTransactionResponseDto(transactionRepository.save(transaction));
    }

    private TransactionResponseDto toTransactionResponseDto(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getType(),
                transaction.getSource().getNumber(),
                transaction.getReceiverNumber(),
                transaction.getAmount(),
                transaction.getTimestamp()
        );
    }

    private Account prepareAccount(String number, String email) {
        return accountRepository.findByNumberAndOwnerEmail(number, email).orElseThrow(
                () -> new RuntimeException("Account not found!")
        );
    }

    private <T extends TransactionRequestDtoInterface> Transaction performTransaction(T transactionDto, Account source) {
        BigDecimal transactionAmount = transactionDto.getAmount();
        BigDecimal sourceBalance = source.getBalance();

        if (sourceBalance.compareTo(transactionAmount) < 0 && !(transactionDto instanceof DepositRequestDto))
            throw new RuntimeException("Insufficient funds!");

        TransactionType type = switch (transactionDto) {
            case WithdrawRequestDto ignored -> TransactionType.WITHDRAW;
            case DepositRequestDto ignored -> TransactionType.DEPOSIT;
            case TransferRequestDto ignored -> TransactionType.TRANSFER;
            default -> throw new RuntimeException("Invalid transaction type!");
        };

        return Transaction.builder()
                .receiverNumber(transactionDto.getReceiverNumber())
                .source(source)
                .amount(transactionDto.getAmount())
                .type(type)
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();
    }
}
