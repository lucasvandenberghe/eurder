package org.switchfully.eurder.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.switchfully.eurder.api.dto.customer.CustomerResponseDTO;
import org.switchfully.eurder.api.mapper.CustomerMapper;
import org.switchfully.eurder.service.CustomerService;
import org.switchfully.eurder.service.request.CreateCustomerRequest;

import java.util.List;

@RestController
@RequestMapping(path = "/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDTO createCustomer(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
        return customerMapper.toResponseDTO(customerService.createCustomer(createCustomerRequest));
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerMapper.toResponseDTO(customerService.getAllCustomers());
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponseDTO getCustomer(@PathVariable long id) {
        return customerMapper.toResponseDTO(customerService.getCustomerById(id));
    }
}
