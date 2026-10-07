package dev.mike.eazybankz.service;

import dev.mike.eazybankz.dto.customer.CustomerResponseDTO;
import dev.mike.eazybankz.dto.customer.CustomerSignUpDTO;
import dev.mike.eazybankz.dto.customer.CustomerUpdateDTO;
import dev.mike.eazybankz.entity.Account;
import dev.mike.eazybankz.entity.Customer;
import dev.mike.eazybankz.entity.enums.Status;
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
    private final PasswordEncoder passwordEncoder;
    private final AccountNumberGenerator accountNumberGenerator;

    // CRUD - Create
    @Transactional
    public CustomerResponseDTO create(CustomerSignUpDTO dto) {
        if (customerRepository.existsByEmail(dto.email()))
            throw new RuntimeException("This email is not available.");

        return toCustomerResponseDTO(customerRepository.save(toCustomer(dto)));
    }

    // CRUD - Read
    public CustomerResponseDTO findByEmail(String email) {
        Customer customer = fetchCustomer(email);

        return toCustomerResponseDTO(customer);
    }

    // CRUD - Update
    @Transactional
    public CustomerResponseDTO update(CustomerUpdateDTO dto, String email) {
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

    // Checking whether customer exists
    private Customer fetchCustomer(String email) {

        return customerRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException(String.format("Customer with email %s not found.", email))
        );
    }

    // Support methods
    private Customer toCustomer(CustomerSignUpDTO dto) {
        String encodedPassword = passwordEncoder.encode(dto.password());

        Customer customer = Customer
                .builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .password(encodedPassword)
                .build();

        Account account = Account
                .builder()
                .IBAN(accountNumberGenerator.generateAccountNumber())
                .currentBalance(BigDecimal.ZERO)
                .build();

        customer.addAccount(account);

        return customer;
    }

    private CustomerResponseDTO toCustomerResponseDTO(Customer customer) {
        return new CustomerResponseDTO(customer.getEmail(), customer.getFirstName(), customer.getLastName());
    }
}
