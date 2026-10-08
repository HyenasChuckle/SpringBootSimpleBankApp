package dev.mike.eazybankz.service;

import dev.mike.eazybankz.dto.customer.CustomerResponseDto;
import dev.mike.eazybankz.dto.customer.CustomerSignUpDto;
import dev.mike.eazybankz.dto.customer.CustomerUpdateDto;
import dev.mike.eazybankz.entity.Account;
import dev.mike.eazybankz.entity.enums.AccountType;
import dev.mike.eazybankz.entity.Customer;
import dev.mike.eazybankz.entity.enums.Status;
import dev.mike.eazybankz.repository.AccountRepository;
import dev.mike.eazybankz.repository.CustomerRepository;
import dev.mike.eazybankz.util.AccountNumberGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountNumberGenerator accountNumberGenerator;

    // CRUD - Create
    @Transactional
    public CustomerResponseDto create(CustomerSignUpDto dto) {
        if (customerRepository.existsByEmail(dto.email()))
            throw new RuntimeException("This email is not available.");

        return toCustomerResponseDTO(customerRepository.save(toCustomer(dto)));
    }

    // CRUD - Read
    public CustomerResponseDto findByEmail(String email) {
        Customer customer = fetchCustomer(email);

        return toCustomerResponseDTO(customer);
    }

    // CRUD - Update
    @Transactional
    public CustomerResponseDto update(CustomerUpdateDto dto, String email) {
        Customer customer = fetchCustomer(email);

        if (!customer.getStatus().equals(Status.ACTIVE))
            throw new RuntimeException("Unable to perform the update.");

        if (dto.firstName() != null && !dto.firstName().isBlank())
            customer.setFirstName(dto.firstName());

        if (dto.lastName() != null && !dto.lastName().isBlank())
            customer.setLastName(dto.lastName());

        if (dto.email() != null && !dto.email().isBlank())
            customer.setEmail(dto.email());

        if (dto.password() != null && !dto.password().isBlank()) {
            String encodedPassword = passwordEncoder.encode(dto.password());
            customer.setPassword(encodedPassword);
        }

        return toCustomerResponseDTO(customer);
    }

    // CRUD - Delete
    @Transactional
    public void delete(String email) {
        Customer customer = fetchCustomer(email);

        if (!customer.getStatus().equals(Status.ACTIVE))
            throw new RuntimeException("This customer is already deactivated.");

        customer.setStatus(Status.DEACTIVATED);
    }

    private Customer fetchCustomer(String email) {
        return customerRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException(String.format("Customer with email %s not found.", email))
        );
    }

    private Customer toCustomer(CustomerSignUpDto dto) {
        Customer customer = Customer.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .build();

        Account account = Account.builder()
                .number(accountNumberGenerator.generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .owner(customer)
                .type(AccountType.CHECKING)
                .build();

        accountRepository.save(account);

        return customer;
    }

    private CustomerResponseDto toCustomerResponseDTO(Customer customer) {
        return new CustomerResponseDto(
                customer.getEmail(),
                customer.getFirstName(),
                customer.getLastName()
        );
    }
}
