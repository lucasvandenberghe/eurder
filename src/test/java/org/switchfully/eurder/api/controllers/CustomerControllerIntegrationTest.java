package org.switchfully.eurder.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.switchfully.eurder.api.dtos.customer.CustomerResponseDTO;
import org.switchfully.eurder.api.mappers.CustomerMapper;
import org.switchfully.eurder.domain.Customer;
import org.switchfully.eurder.repositories.CustomerRepository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;

    @BeforeEach
    void setupCustomerRepository() {
        customerRepository.clear();
    }

    @Nested
    @DisplayName("When creating a customer")
    public class CustomerCreation {

        @Test
        void givenLegalRequestBody_whenCreatingCustomer_thenCustomerIsCreatedWith201StatusCodeAndSavedInRepository() throws Exception {
            // Given
            final String legalRequestBodyJson = """
                    {
                        "firstName": "James",
                        "lastName": "Bond",
                        "emailAddress": "james.bond@mi6.uk",
                        "address": "85 Albert Embankment, London SE11 5AW, United Kingdom",
                        "phoneNumber": "+32477777777"
                    }
                    """;

            // When + Then
            ResultActions result = mockMvc.perform(post("/customers")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(legalRequestBodyJson));

            result.andExpectAll(
                    status().isCreated(),
                    content().contentType(MediaType.APPLICATION_JSON),
                    jsonPath("$.id").isNotEmpty(),
                    jsonPath("$.firstName").value("James"),
                    jsonPath("$.lastName").value("Bond"),
                    jsonPath("$.emailAddress").value("james.bond@mi6.uk"),
                    jsonPath("$.address").value("85 Albert Embankment, London SE11 5AW, United Kingdom"),
                    jsonPath("$.phoneNumber").value("+32477777777")
            );

            UUID customerResponseDTOId = objectMapper.readValue(result.andReturn()
                            .getResponse()
                            .getContentAsString(), CustomerResponseDTO.class)
                    .id();

            assertThat(customerRepository.existsById(customerResponseDTOId)).isTrue();

            Customer savedCustomer = customerRepository.getById(customerResponseDTOId).get();

            assertThat(savedCustomer.getFirstName()).isEqualTo("James");
            assertThat(savedCustomer.getLastName()).isEqualTo("Bond");
            assertThat(savedCustomer.getEmailAddress()).isEqualTo("james.bond@mi6.uk");
            assertThat(savedCustomer.getAddress()).isEqualTo("85 Albert Embankment, London SE11 5AW, United Kingdom");
            assertThat(savedCustomer.getPhoneNumber()).isEqualTo("+32477777777");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                """
                        {
                            "lastName": "Bond",
                            "emailAddress": "james.bond@mi6.uk",
                            "address": "85 Albert Embankment, London SE11 5AW, United Kingdom",
                            "phoneNumber": "+32477777777"
                        }
                        """,
                """
                        {
                            "firstName": "James",
                            "emailAddress": "james.bond@mi6.uk",
                            "address": "85 Albert Embankment, London SE11 5AW, United Kingdom",
                            "phoneNumber": "+32477777777"
                        }
                        """,
                """
                        {
                            "firstName": "James",
                            "lastName": "Bond",
                            "address": "85 Albert Embankment, London SE11 5AW, United Kingdom",
                            "phoneNumber": "+32477777777"
                        }
                        """,
                """
                        {
                            "firstName": "James",
                            "lastName": "Bond",
                            "emailAddress": "james.bond@mi6.uk",
                            "phoneNumber": "+32477777777"
                        }
                        """,
                """
                        {
                            "firstName": "James",
                            "lastName": "Bond",
                            "emailAddress": "james.bond@mi6.uk",
                            "address": "85 Albert Embankment, London SE11 5AW, United Kingdom"
                        }
                        """
        })
        void givenMissingRequiredField_whenCreatingCustomer_thenBadRequest(String requestBodyJSONWithMissingField) throws Exception {
            // Given + When + Then
            mockMvc.perform(post("/customers")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBodyJSONWithMissingField))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   ", "\n", "\t"})
        void givenIllegalFirstName_whenCreatingCustomer_thenBadRequest(String illegalFirstNameFieldValue) throws Exception {
            // Given
            final String illegalRequestBodyJson = """
                    {
                        "firstName": "%s",
                        "lastName": "Bond",
                        "emailAddress": "james.bond@mi6.uk",
                        "address": "85 Albert Embankment, London SE11 5AW, United Kingdom",
                        "phoneNumber": "+32477777777"
                    }
                    """.formatted(illegalFirstNameFieldValue);

            // Given + When + Then
            mockMvc.perform(post("/customers")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(illegalRequestBodyJson))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   ", "\n", "\t"})
        void givenIllegalLastName_whenCreatingCustomer_thenBadRequest(String illegalLastNameFieldValue) throws Exception {
            // Given
            final String illegalRequestBodyJson = """
                    {
                        "firstName": "James",
                        "lastName": "%s",
                        "emailAddress": "james.bond@mi6.uk",
                        "address": "85 Albert Embankment, London SE11 5AW, United Kingdom",
                        "phoneNumber": "+32477777777"
                    }
                    """.formatted(illegalLastNameFieldValue);

            // Given + When + Then
            mockMvc.perform(post("/customers")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(illegalRequestBodyJson))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   ", "\n", "\t"})
        void givenIllegalEmailAddress_whenCreatingCustomer_thenBadRequest(String illegalEmailAddressFieldValue) throws Exception {
            // Given
            final String illegalRequestBodyJson = """
                    {
                        "firstName": "James",
                        "lastName": "Bond",
                        "emailAddress": "%s",
                        "address": "85 Albert Embankment, London SE11 5AW, United Kingdom",
                        "phoneNumber": "+32477777777"
                    }
                    """.formatted(illegalEmailAddressFieldValue);

            // Given + When + Then
            mockMvc.perform(post("/customers")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(illegalRequestBodyJson))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   ", "\n", "\t"})
        void givenIllegalAddress_whenCreatingCustomer_thenBadRequest(String illegalAddressFieldValue) throws Exception {
            // Given
            final String illegalRequestBodyJson = """
                    {
                        "firstName": "James",
                        "lastName": "Bond",
                        "emailAddress": "james.bond@mi6.uk",
                        "address": "%s",
                        "phoneNumber": "+32477777777"
                    }
                    """.formatted(illegalAddressFieldValue);

            // Given + When + Then
            mockMvc.perform(post("/customers")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(illegalRequestBodyJson))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   ", "\n", "\t"})
        void givenIllegalPhoneNumber_whenCreatingCustomer_thenBadRequest(String illegalPhoneNumberFieldValue) throws Exception {
            // Given
            final String illegalRequestBodyJson = """
                    {
                        "firstName": "James",
                        "lastName": "Bond",
                        "emailAddress": "james.bond@mi6.uk",
                        "address": "85 Albert Embankment, London SE11 5AW, United Kingdom",
                        "phoneNumber": "%s"
                    }
                    """.formatted(illegalPhoneNumberFieldValue);

            // Given + When + Then
            mockMvc.perform(post("/customers")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(illegalRequestBodyJson))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void givenCustomersInRepository_whenGetCustomers_thenListOfCustomersWith200Status() throws Exception {
        // Given
        Customer customer1 = new Customer("James",
                "Bond",
                "james.bond@mi6.uk",
                "85 Albert Embankment, London SE11 5AW, United Kingdom",
                "+32477777777");

        Customer customer2 = new Customer("Olivia",
                "Mansfield",
                "M@mi6.uk",
                "85 Albert Embankment, London SE11 5AW, United Kingdom",
                "+32400000000");

        customerRepository.putCustomer(customer1);
        customerRepository.putCustomer(customer2);

        // When
        ResultActions result = mockMvc.perform(get("/customers")
                .accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<CustomerResponseDTO> actualCustomers = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(actualCustomers)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(customerMapper.toResponseDTO(customer1), customerMapper.toResponseDTO(customer2)));
    }


}