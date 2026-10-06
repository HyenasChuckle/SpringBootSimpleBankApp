package dev.mike.easybank.service;

import dev.mike.easybank.dto.customer.CustomerResponseDTO;
import dev.mike.easybank.dto.customer.CustomerSignUpDTO;
import dev.mike.easybank.dto.customer.UpdateCustomerDTO;
import dev.mike.easybank.entity.Account;
import dev.mike.easybank.entity.Customer;
import dev.mike.easybank.repository.CustomerRepository;
import dev.mike.easybank.util.AccountNumberGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountNumberGenerator accountNumberGenerator;

    // Saving new customer in database.
    @Transactional
    public CustomerResponseDTO create(CustomerSignUpDTO customerSignUpDTO) {
        Customer customer = toCustomer(customerSignUpDTO);
        customerRepository.save(customer);

        return toCustomerResponseDTO(customer);
    }

    // Update customer in database.
    @Transactional
    public CustomerResponseDTO update(UpdateCustomerDTO updateCustomerDTO, String username) {
        Customer customer = customerRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("user not found")
        );

        if (updateCustomerDTO.username() != null && !updateCustomerDTO.username().isBlank())
            customer.setUsername(updateCustomerDTO.username());

        if (updateCustomerDTO.password() != null && !updateCustomerDTO.password().isBlank())
            customer.setPassword(passwordEncoder.encode(updateCustomerDTO.password()));

        return toCustomerResponseDTO(customerRepository.save(customer));
    }

    // Delete user from database.
    @Transactional
    public void delete(String username) {
        Customer customer = customerRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("user not found")
        );

        customerRepository.delete(customer);
    }

    // DTO mapping methods
    private Customer toCustomer(CustomerSignUpDTO customerSignUpDTO) {
        String encodedPassword = passwordEncoder.encode(customerSignUpDTO.password());

        Customer customer = Customer
                .builder()
                .username(customerSignUpDTO.username())
                .password(encodedPassword)
                .build();

        Account account = Account
                .builder()
                .number(accountNumberGenerator.generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .build();

        customer.addAccount(account);

        return customer;
    }

    private CustomerResponseDTO toCustomerResponseDTO(Customer customer) {
        return new CustomerResponseDTO(customer.getUsername());
    }
}
