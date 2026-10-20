package org.switchfully.eurder.services;

import org.springframework.stereotype.Service;
import org.switchfully.eurder.domain.Customer;
import org.switchfully.eurder.repositories.CustomerRepository;
import org.switchfully.eurder.services.exceptions.CustomerServiceException;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsById(customer.getId()))
            throw new CustomerServiceException("Customer with that id already exists.");

        return customerRepository.putCustomer(customer);
    }

    public List<Customer> getCustomers() {
        return customerRepository.getAllCustomers();
    }
}
