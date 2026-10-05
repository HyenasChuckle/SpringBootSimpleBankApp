package dev.mike.easybank.controller;

import dev.mike.easybank.dto.account.AccountResponseDTO;
import dev.mike.easybank.service.AccountService;
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
    public ResponseEntity<AccountResponseDTO> createAccount(Principal principal) {
        String username = principal.getName();

        AccountResponseDTO accountResponseDTO = accountService.create(username);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponseDTO);
    }
}
