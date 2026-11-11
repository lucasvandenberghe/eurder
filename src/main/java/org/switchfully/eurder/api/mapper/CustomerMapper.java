package org.switchfully.eurder.api.mapper;

import org.springframework.stereotype.Component;
import org.switchfully.eurder.api.dto.customer.CustomerResponseDTO;
import org.switchfully.eurder.domain.Customer;

import java.util.List;

@Component
public class CustomerMapper {
    public CustomerResponseDTO toResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmailAddress(),
                customer.getAddress(),
                customer.getPhoneNumber()
        );
    }

    public List<CustomerResponseDTO> toResponseDTO(List<Customer> customers) {
        return customers.stream().map(this::toResponseDTO).toList();
    }
}
