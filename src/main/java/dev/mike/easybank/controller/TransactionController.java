package dev.mike.easybank.controller;

import dev.mike.easybank.dto.transaction.DepositRequestDto;
import dev.mike.easybank.dto.transaction.TransactionResponseDto;
import dev.mike.easybank.dto.transaction.WithdrawRequestDto;
import dev.mike.easybank.service.TransactionService;
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
    public ResponseEntity<TransactionResponseDto> deposit(@RequestBody DepositRequestDto depositRequestDto, Principal principal) {
        String username = principal.getName();

        TransactionResponseDto response = transactionService.createDeposit(depositRequestDto, username);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDto> withdraw(@RequestBody WithdrawRequestDto withdrawRequestDto, Principal principal) {
        String username = principal.getName();

        TransactionResponseDto response = transactionService.createWithdraw(withdrawRequestDto, username);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
