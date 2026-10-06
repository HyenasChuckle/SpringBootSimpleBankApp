package dev.mike.easybank.controller;

import dev.mike.easybank.dto.customer.CustomerResponseDTO;
import dev.mike.easybank.dto.customer.CustomerSignUpDTO;
import dev.mike.easybank.dto.customer.UpdateCustomerDTO;
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
    public ResponseEntity<CustomerResponseDTO> registerCustomer(@RequestBody CustomerSignUpDTO customerSignUpDTO) {
        CustomerResponseDTO customerResponseDTO = customerService.create(customerSignUpDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(customerResponseDTO);
    }

    // Updating existing (currently signed in) customer.
    @PutMapping
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@RequestBody UpdateCustomerDTO updateCustomerDTO,
                                                              Principal principal) {
        CustomerResponseDTO customerResponseDTO = customerService.update(updateCustomerDTO, principal.getName());

        return ResponseEntity.status(HttpStatus.OK).body(customerResponseDTO);
    }

    // Delete existing (currently signed in) customer.
    @DeleteMapping
    public ResponseEntity<Void> deleteCustomer(Principal principal) {
        customerService.delete(principal.getName());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
