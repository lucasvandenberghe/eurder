package org.switchfully.eurder.api.mappers;

import org.springframework.stereotype.Component;
import org.switchfully.eurder.api.dtos.customer.CustomerCreationRequestDTO;
import org.switchfully.eurder.api.dtos.customer.CustomerResponseDTO;
import org.switchfully.eurder.domain.Customer;

@Component
public class CustomerMapper {
    public Customer toCustomer(CustomerCreationRequestDTO customerCreationRequestDTO) {
        return new Customer(
                customerCreationRequestDTO.firstName(),
                customerCreationRequestDTO.lastName(),
                customerCreationRequestDTO.emailAddress(),
                customerCreationRequestDTO.address(),
                customerCreationRequestDTO.phoneNumber()
        );
    }

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
}
