package dev.mike.easybank.controller;

import dev.mike.easybank.dto.CustomerResponseDTO;
import dev.mike.easybank.dto.RegisterCustomerDTO;
import dev.mike.easybank.dto.UpdateCustomerDTO;
import dev.mike.easybank.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // Creating new customer.
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> registerCustomer(@RequestBody RegisterCustomerDTO registerCustomerDTO) {
        CustomerResponseDTO customerResponseDTO = customerService.create(registerCustomerDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(customerResponseDTO);
    }

    // Updating existing customer.
    @PutMapping
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@RequestBody UpdateCustomerDTO updateCustomerDTO,
                                                              Principal principal) {
        CustomerResponseDTO customerResponseDTO = customerService.update(updateCustomerDTO, principal.getName());

        return ResponseEntity.status(HttpStatus.OK).body(customerResponseDTO);
    }
}
