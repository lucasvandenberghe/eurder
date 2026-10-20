package org.switchfully.eurder.api.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.switchfully.eurder.api.dtos.customer.CustomerCreationRequestDTO;
import org.switchfully.eurder.api.dtos.customer.CustomerResponseDTO;
import org.switchfully.eurder.api.mappers.CustomerMapper;
import org.switchfully.eurder.services.CustomerService;

@RestController
@RequestMapping(path = "/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDTO createCustomer(@Valid @RequestBody CustomerCreationRequestDTO customerCreationRequestDTO) {
        return customerMapper.toResponseDTO(customerService.createCustomer(customerMapper.toCustomer(customerCreationRequestDTO)));
    }
}
