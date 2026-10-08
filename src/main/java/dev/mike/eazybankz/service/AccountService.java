package dev.mike.eazybankz.service;

import dev.mike.eazybankz.dto.account.AccountResponseDto;
import dev.mike.eazybankz.entity.Account;
import dev.mike.eazybankz.entity.enums.AccountType;
import dev.mike.eazybankz.entity.Customer;
import dev.mike.eazybankz.repository.AccountRepository;
import dev.mike.eazybankz.repository.CustomerRepository;
import dev.mike.eazybankz.util.AccountNumberGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator ;

    @Transactional
    public AccountResponseDto create(String email) {
        Customer customer = customerRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException(String.format("Customer with email %s not found.", email))
        );

        Account account = Account.builder()
                .number(accountNumberGenerator.generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .owner(customer)
                .type(AccountType.SAVINGS)
                .build();

        return toAccountResponseDTO(accountRepository.save(account));
    }

    private AccountResponseDto toAccountResponseDTO(Account account) {
        return new AccountResponseDto(
                account.getNumber(),
                account.getBalance()
        );
    }
}
