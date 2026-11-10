package org.switchfully.eurder.service;

import org.springframework.stereotype.Service;
import org.switchfully.eurder.domain.Customer;
import org.switchfully.eurder.repository.CustomerRepository;
import org.switchfully.eurder.service.exception.NotFoundException;
import org.switchfully.eurder.service.request.CreateCustomerRequest;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(CreateCustomerRequest createCustomerRequest) {
        Customer customer = new Customer(
                createCustomerRequest.firstName(),
                createCustomerRequest.lastName(),
                createCustomerRequest.emailAddress(),
                createCustomerRequest.address(),
                createCustomerRequest.phoneNumber()
        );

        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> NotFoundException.customerNotFound(id));
    }
}
