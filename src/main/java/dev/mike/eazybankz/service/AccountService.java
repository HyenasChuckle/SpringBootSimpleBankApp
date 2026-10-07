package dev.mike.eazybankz.service;

import dev.mike.eazybankz.dto.account.AccountResponseDTO;
import dev.mike.eazybankz.entity.Account;
import dev.mike.eazybankz.entity.Customer;
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
    private final AccountNumberGenerator accountNumberGenerator ;

    @Transactional
    public AccountResponseDTO create(String email) {
        Customer customer = customerRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException(String.format("Customer with email %s not found.", email))
        );

        Account account = Account
                .builder()
                .IBAN(accountNumberGenerator.generateAccountNumber())
                .currentBalance(BigDecimal.ZERO)
                .build();

        customer.addAccount(account);

        return toAccountResponseDTO(account);
    }

    private AccountResponseDTO toAccountResponseDTO(Account account) {
        return new AccountResponseDTO(account.getIBAN());
    }
}
