package dev.mike.easybank.service;

import dev.mike.easybank.dto.account.AccountResponseDTO;
import dev.mike.easybank.entity.Account;
import dev.mike.easybank.entity.Customer;
import dev.mike.easybank.repository.AccountRepository;
import dev.mike.easybank.repository.CustomerRepository;
import dev.mike.easybank.util.AccountNumberGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountNumberGenerator accountNumberGenerator ;

    // Save new account in db.
    @Transactional
    public AccountResponseDTO create(String username) {
        Customer customer = customerRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("user not found")
        );

        Account account = Account
                .builder()
                .number(accountNumberGenerator.generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .build();

        customer.addAccount(account);

        return toAccountResponseDTO(accountRepository.save(account));
    }

    // Mapping methods.
    private AccountResponseDTO toAccountResponseDTO(Account account) {
        return new AccountResponseDTO(account.getNumber());
    }
}
