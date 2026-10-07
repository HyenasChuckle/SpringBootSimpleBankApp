package dev.mike.eazybankz.controller;

import dev.mike.eazybankz.dto.transaction.DepositRequestDto;
import dev.mike.eazybankz.dto.transaction.TransactionResponseDto;
import dev.mike.eazybankz.dto.transaction.TransferRequestDto;
import dev.mike.eazybankz.dto.transaction.WithdrawRequestDto;
import dev.mike.eazybankz.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> deposit(@RequestBody DepositRequestDto dto, Principal principal) {
        String username = principal.getName();

        TransactionResponseDto response = transactionService.createDeposit(dto, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDto> withdraw(@RequestBody WithdrawRequestDto dto, Principal principal) {
        String username = principal.getName();

        TransactionResponseDto response = transactionService.createWithdraw(dto, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> withdraw(@RequestBody TransferRequestDto dto, Principal principal) {
        String username = principal.getName();

        TransactionResponseDto response = transactionService.createTransfer(dto, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
