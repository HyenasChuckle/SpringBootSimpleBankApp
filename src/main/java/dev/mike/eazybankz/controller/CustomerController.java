package dev.mike.eazybankz.controller;

import dev.mike.eazybankz.dto.customer.CustomerResponseDto;
import dev.mike.eazybankz.dto.customer.CustomerSignUpDto;
import dev.mike.eazybankz.dto.customer.CustomerUpdateDto;
import dev.mike.eazybankz.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // CRUD - Create
    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDto> registerCustomer(@RequestBody @Valid CustomerSignUpDto dto) {
        CustomerResponseDto responseDto = customerService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // CRUD - Read
    @GetMapping("/me")
    public ResponseEntity<CustomerResponseDto> getCustomer(Principal principal) {
        CustomerResponseDto responseDto = customerService.findByEmail(principal.getName());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // CRUD - Update
    @PutMapping("/update")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@RequestBody CustomerUpdateDto dto, Principal principal) {
        CustomerResponseDto responseDto = customerService.update(dto, principal.getName());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // CRUD - Delete
    @DeleteMapping("/deactivate")
    public ResponseEntity<Void> deactivateCustomer(Principal principal) {
        customerService.delete(principal.getName());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
