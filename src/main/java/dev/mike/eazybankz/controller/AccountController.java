package dev.mike.eazybankz.controller;

import dev.mike.eazybankz.dto.account.AccountResponseDto;
import dev.mike.eazybankz.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(Principal principal) {
        AccountResponseDto responseDto = accountService.create(principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
