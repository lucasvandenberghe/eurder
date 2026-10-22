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
import org.springframework.test.web.servlet.ResultActions;
import org.switchfully.eurder.domain.Customer;
import org.switchfully.eurder.domain.Item;
import org.switchfully.eurder.repositories.CustomerRepository;
import org.switchfully.eurder.repositories.ItemRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

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

        @Test
        void givenKnownCustomerAndKnownItemInStock_whenCreateOrder_thenOrderCreatedWith201Status() throws Exception {
            // Given
            itemRepository.putItem(dummyItem);
            customerRepository.putCustomer(dummyCustomer);

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
            itemRepository.putItem(dummyItem);
            customerRepository.putCustomer(dummyCustomer);

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
    }

}