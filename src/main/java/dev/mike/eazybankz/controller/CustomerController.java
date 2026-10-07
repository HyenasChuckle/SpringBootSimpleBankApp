package dev.mike.eazybankz.controller;

import dev.mike.eazybankz.dto.customer.CustomerResponseDTO;
import dev.mike.eazybankz.dto.customer.CustomerSignUpDTO;
import dev.mike.eazybankz.dto.customer.CustomerUpdateDTO;
import dev.mike.eazybankz.service.CustomerService;
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

    // CRUD - Create
    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDTO> registerCustomer(@RequestBody CustomerSignUpDTO dto) {
        CustomerResponseDTO responseDto = customerService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // CRUD - Read
    @GetMapping("/me")
    public ResponseEntity<CustomerResponseDTO> getCustomer(Principal principal) {
        CustomerResponseDTO responseDto = customerService.findByEmail(principal.getName());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // CRUD - Update
    @PutMapping("/update")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@RequestBody CustomerUpdateDTO dto, Principal principal) {
        CustomerResponseDTO responseDto = customerService.update(dto, principal.getName());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // CRUD - Delete
    @DeleteMapping("/deactivate")
    public ResponseEntity<Void> deactivateCustomer(Principal principal) {
        customerService.delete(principal.getName());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
