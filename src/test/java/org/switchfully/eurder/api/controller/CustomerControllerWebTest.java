package org.switchfully.eurder.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.switchfully.eurder.api.dto.customer.CustomerResponseDTO;
import org.switchfully.eurder.api.mapper.CustomerMapper;
import org.switchfully.eurder.domain.Customer;
import org.switchfully.eurder.service.CustomerService;
import org.switchfully.eurder.service.exception.NotFoundException;
import org.switchfully.eurder.service.request.CreateCustomerRequest;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private CustomerMapper customerMapper;

    @Test
    void givenValidRequest_whenCreateCustomer_thenCustomerReturnedWith201() throws Exception {
        Customer mockCustomer = new Customer(
                "test",
                "testtest",
                "test@test.test",
                "teststreet",
                "0123456789"
        );
        ReflectionTestUtils.setField(mockCustomer, "id", 1L);

        when(customerService.createCustomer(any(CreateCustomerRequest.class)))
                .thenReturn(mockCustomer);

        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(
                mockCustomer.getId(),
                mockCustomer.getFirstName(),
                mockCustomer.getLastName(),
                mockCustomer.getEmailAddress(),
                mockCustomer.getAddress(),
                mockCustomer.getPhoneNumber()
        );

        when(customerMapper.toResponseDTO(mockCustomer))
                .thenReturn(customerResponseDTO);

        mockMvc.perform(post("/customers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "test",
                                    "lastName": "testtest",
                                    "emailAddress": "test@test.test",
                                    "address": "teststreet",
                                    "phoneNumber": "0123456789"
                                }
                                """))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(1L)
                );
    }

    @Test
    void givenInvalidRequest_whenCreateCustomer_thenBadRequest() throws Exception {
        mockMvc.perform(post("/customers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "lastName": "testtest",
                                    "emailAddress": "test@test.test",
                                    "address": "teststreet",
                                    "phoneNumber": "0123456789"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenIdOfKnownCustomer_whenGetCustomer_thenCustomerReturnedWith200() throws Exception {
        long customerId = 1L;

        Customer mockCustomer = new Customer(
                "test",
                "testtest",
                "test@test.test",
                "teststreet",
                "0123456789"
        );
        ReflectionTestUtils.setField(mockCustomer, "id", customerId);

        when(customerService.getCustomerById(customerId))
                .thenReturn(mockCustomer);

        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(
                mockCustomer.getId(),
                mockCustomer.getFirstName(),
                mockCustomer.getLastName(),
                mockCustomer.getEmailAddress(),
                mockCustomer.getAddress(),
                mockCustomer.getPhoneNumber()
        );

        when(customerMapper.toResponseDTO(mockCustomer))
                .thenReturn(customerResponseDTO);

        mockMvc.perform(get("/customers/{id}", customerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(customerId));
    }

    @Test
    void givenIdOfUnknownCustomer_whenGetCustomer_thenNotFound() throws Exception {
        long nonExistentId = 1L;

        when(customerService.getCustomerById(nonExistentId))
                .thenThrow(NotFoundException.customerNotFound(nonExistentId));

        mockMvc.perform(get("/customers/{id}", nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(
                        status().isNotFound());
    }

    @Test
    void givenSavedCustomers_whenGetCustomers_thenListOfCustomersReturnedWith200() throws Exception {
        Customer mockCustomer1 = new Customer(
                "test1",
                "testtest1",
                "test1@test.test",
                "teststreet1",
                "0123456781"
        );
        ReflectionTestUtils.setField(mockCustomer1, "id", 1L);

        Customer mockCustomer2 = new Customer(
                "test2",
                "testtest2",
                "test2@test.test",
                "teststreet2",
                "0123456782"
        );
        ReflectionTestUtils.setField(mockCustomer2, "id", 2L);

        when(customerService.getAllCustomers())
                .thenReturn(List.of(mockCustomer1, mockCustomer2));

        when(customerMapper.toResponseDTO(anyList()))
                .thenReturn(List.of(new CustomerResponseDTO(
                        mockCustomer1.getId(),
                        mockCustomer1.getFirstName(),
                        mockCustomer1.getLastName(),
                        mockCustomer1.getEmailAddress(),
                        mockCustomer1.getAddress(),
                        mockCustomer1.getPhoneNumber()
                ), new CustomerResponseDTO(
                        mockCustomer2.getId(),
                        mockCustomer2.getFirstName(),
                        mockCustomer2.getLastName(),
                        mockCustomer2.getEmailAddress(),
                        mockCustomer2.getAddress(),
                        mockCustomer2.getPhoneNumber()
                )));

        mockMvc.perform(get("/customers")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(2),
                jsonPath("$[?(@.id == 1)].firstName").value("test1"),
                jsonPath("$[?(@.id == 2)].firstName").value("test2"),
                jsonPath("$[?(@.id == 1)].lastName").value("testtest1"),
                jsonPath("$[?(@.id == 2)].lastName").value("testtest2"),
                jsonPath("$[?(@.id == 1)].emailAddress").value("test1@test.test"),
                jsonPath("$[?(@.id == 2)].emailAddress").value("test2@test.test"),
                jsonPath("$[?(@.id == 1)].address").value("teststreet1"),
                jsonPath("$[?(@.id == 2)].address").value("teststreet2"),
                jsonPath("$[?(@.id == 1)].phoneNumber").value("0123456781"),
                jsonPath("$[?(@.id == 2)].phoneNumber").value("0123456782")
        );
    }
}
