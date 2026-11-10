package org.switchfully.eurder.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.switchfully.eurder.domain.Customer;
import org.switchfully.eurder.repository.CustomerRepository;
import org.switchfully.eurder.service.exception.NotFoundException;
import org.switchfully.eurder.service.request.CreateCustomerRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(CustomerService.class)
class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void givenValidRequest_whenCreateCustomer_thenCustomerCreatedSavedAndReturned() {
        CreateCustomerRequest request = new CreateCustomerRequest(
                "testfirst",
                "testlast",
                "test@test.test",
                "teststreet 201 testcountry",
                "0123456789"
        );

        Customer customer = customerService.createCustomer(request);

        assertThat(customer)
                .isNotNull();
        assertThat(customer.getId())
                .isNotNegative();
        assertThat(customer.getFirstName())
                .isEqualTo("testfirst");
        assertThat(customer.getLastName())
                .isEqualTo("testlast");
        assertThat(customer.getEmailAddress())
                .isEqualTo("test@test.test");
        assertThat(customer.getAddress())
                .isEqualTo("teststreet 201 testcountry");
        assertThat(customer.getPhoneNumber())
                .isEqualTo("0123456789");

        Customer savedCustomer = customerRepository.findById(customer.getId()).orElse(null);

        assertThat(savedCustomer)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(customer);
    }

    @Test
    void givenIdOfExistingCustomer_whenGetCustomerById_thenReturnExistingCustomer() {
        Customer customer = customerRepository.save(new Customer(
                "testfirst",
                "testlast",
                "test@test.test",
                "teststreet 201 testcountry",
                "0123456789"
        ));

        Customer foundCustomer = customerService.getCustomerById(customer.getId());

        assertThat(foundCustomer)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(customer);
    }

    @Test
    void givenIdOfNonExistingCustomer_whenGetCustomerById_thenThrowNotFoundException() {
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    customerService.getCustomerById(0L);
                })
                .withMessage("Customer with id 0 not found.");
    }

    @Test
    void givenCustomersInDatabase_whenGetCustomers_thenReturnListOfCustomers() {
        Customer customer1 = customerRepository.save(new Customer(
                "testfirst1",
                "testlast1",
                "test@test.test1",
                "teststreet 201 testcountry1",
                "0123456781"
        ));

        Customer customer2 = customerRepository.save(new Customer(
                "testfirst2",
                "testlast2",
                "test@test.test2",
                "teststreet 201 testcountry2",
                "0123456782"
        ));

        List<Customer> customers = customerService.getCustomers();

        assertThat(customers)
                .isNotNull();
        assertThat(customers)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(customer1, customer2);
    }
}