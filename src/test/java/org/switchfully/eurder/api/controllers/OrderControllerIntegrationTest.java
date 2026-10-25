package org.switchfully.eurder.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.switchfully.eurder.api.dtos.order.OrderResponseDTO;
import org.switchfully.eurder.domain.Customer;
import org.switchfully.eurder.domain.Item;
import org.switchfully.eurder.repositories.CustomerRepository;
import org.switchfully.eurder.repositories.ItemRepository;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer dummyCustomer;

    private Item dummyItem;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        orderRepository.clear();
        itemRepository.clear();
        customerRepository.clear();

        dummyCustomer = new Customer("Jane",
                "Doe",
                "jane.doe@gmail.com",
                "Koning Albert II Laan 33, 1030 Schaarbeek",
                "+32477777777");

        dummyItem = new Item(
                "Twix",
                "A delicious chocolate bar with caramel and a crunchy bite.",
                1.0,
                153
        );
    }

    @Nested
    @DisplayName("when creating an order")
    public class OrderCreation {

        @Nested
        @DisplayName("given a known customer and known item(s)")
        public class KnownCustomerAndKnownItems {

            @BeforeEach
            void addDummyItemAndDummyCustomerToRepositories() {
                itemRepository.putItem(dummyItem);
                customerRepository.putCustomer(dummyCustomer);
            }

            @Test
            void givenKnownCustomerAndKnownItemInStock_whenCreateOrder_thenOrderCreatedWith201Status() throws Exception {
                // Given
                String orderRequestAsJson = """
                        {
                            "customerId": "%s",
                            "items": [
                                {
                                    "itemId": "%s",
                                    "amount": 5
                                }
                            ]
                        }
                        """.formatted(dummyCustomer.getId(), dummyItem.getId());

                // When + Then
                mockMvc.perform(post("/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestAsJson))
                        .andExpectAll(
                                status().isCreated(),
                                content().contentType(MediaType.APPLICATION_JSON)
                        );
            }

            @Test
            void givenKnownCustomerAndKnownItemInStock_whenCreateOrder_thenShippingDateNextDay() throws Exception {
                // Given
                String orderRequestAsJson = """
                        {
                            "customerId": "%s",
                            "items": [
                                {
                                    "itemId": "%s",
                                    "amount": 5
                                }
                            ]
                        }
                        """.formatted(dummyCustomer.getId(), dummyItem.getId());

                // When
                LocalDate dateBeforeRequest = LocalDate.now();

                MvcResult result = mockMvc.perform(post("/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestAsJson))
                        .andReturn();

                LocalDate dateAfterRequest = LocalDate.now();

                // Then
                OrderResponseDTO orderResponseDTO = objectMapper.convertValue(result.getResponse().getContentAsString(), OrderResponseDTO.class);

                assertThat(orderResponseDTO.itemGroups().getFirst().shippingDate())
                        .isBetween(dateBeforeRequest.plusDays(1), dateAfterRequest.plusDays(1));
            }

            @Test
            void givenKnownCustomerAndNotEnoughOfKnownItemInStock_whenCreateOrder_thenShippingDateNextWeek() throws Exception {
                // Given
                String orderRequestAsJson = """
                        {
                            "customerId": "%s",
                            "items": [
                                {
                                    "itemId": "%s",
                                    "amount": %d
                                }
                            ]
                        }
                        """.formatted(dummyCustomer.getId(), dummyItem.getId(), dummyItem.getAmount() + 1);

                // When
                LocalDate dateBeforeRequest = LocalDate.now();

                MvcResult result = mockMvc.perform(post("/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestAsJson))
                        .andReturn();

                LocalDate dateAfterRequest = LocalDate.now();

                // Then
                OrderResponseDTO orderResponseDTO = objectMapper.convertValue(result.getResponse().getContentAsString(), OrderResponseDTO.class);

                assertThat(orderResponseDTO.itemGroups().getFirst().shippingDate())
                        .isBetween(dateBeforeRequest.plusDays(7), dateAfterRequest.plusDays(7));
            }

            @Test
            void givenKnownCustomerAndKnownItems_whenCreateOrder_thenTotalPriceCalculated() throws Exception {
                // Given
                final Item anotherDummyItem = new Item(
                        "Bounty",
                        "A chocolate bar with coconut flavor.",
                        0.8,
                        13
                );

                final int dummyItemAmount = 20;
                final int anotherDummyItemAmount = 15;

                itemRepository.putItem(anotherDummyItem);

                String orderRequestAsJson = """
                        {
                            "customerId": "%s",
                            "items": [
                                {
                                    "itemId": "%s",
                                    "amount": %d
                                },
                                {
                                    "itemId": "%s",
                                    "amount": %d
                                },
                            ]
                        }
                        """.formatted(dummyCustomer.getId(),
                        dummyItem.getId(),
                        dummyItemAmount,
                        anotherDummyItem.getId(),
                        anotherDummyItemAmount);

                // When
                MvcResult result = mockMvc.perform(post("/orders")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequestAsJson))
                        .andReturn();

                // Then
                OrderResponseDTO orderResponseDTO = objectMapper.convertValue(result.getResponse().getContentAsString(), OrderResponseDTO.class);

                assertThat(orderResponseDTO.totalPrice())
                        .isEqualTo(dummyItem.getPrice() * dummyItemAmount + anotherDummyItem.getPrice() * anotherDummyItemAmount);
            }
        }

        @Test
        void givenUnknownCustomer_whenCreateOrder_thenBadRequest() throws Exception {
            // Given
            itemRepository.putItem(dummyItem);

            String orderRequestAsJson = """
                        {
                            "customerId": "%s",
                            "items": [
                                {
                                    "itemId": "%s",
                                    "amount": 5
                                }
                            ]
                        }
                        """.formatted(UUID.randomUUID(), dummyItem.getId());

            // When + Then
            mockMvc.perform(post("/orders")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(orderRequestAsJson))
                    .andExpect(status().isBadRequest());
        }
    }
}