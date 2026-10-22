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
import org.switchfully.eurder.api.dtos.item.ItemResponseDTO;
import org.switchfully.eurder.api.mappers.ItemMapper;
import org.switchfully.eurder.domain.Item;
import org.switchfully.eurder.repositories.ItemRepository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemMapper itemMapper;

    @BeforeEach
    void setupItemRepository() {
        itemRepository.clear();
    }

    @Nested
    @DisplayName("when creating an item")
    public class ItemCreation {

        @Test
        void givenLegalValues_whenCreateItem_thenItemCreatedWith201StatusCodeAndSavedInRepository() throws Exception {
            // Given
            String legalRequestBodyJson =
                    """
                            {
                                "name": "Twix",
                                "description": "A delicious chocolate bar with caramel and a crunchy bite.",
                                "price": 1.0,
                                "amount": 153
                            }
                            """;

            // When
            ResultActions result = mockMvc.perform(post("/items")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(legalRequestBodyJson));

            // Then
            result.andExpectAll(
                    status().isCreated(),
                    content().contentType(MediaType.APPLICATION_JSON),
                    jsonPath("$.id").isNotEmpty(),
                    jsonPath("$.name").value("Twix"),
                    jsonPath("$.description").value("A delicious chocolate bar with caramel and a crunchy bite."),
                    jsonPath("$.price").value(1.0),
                    jsonPath("$.amount").value(153)
            );

            UUID itemId = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ItemResponseDTO.class)
                    .id();

            assertThat(itemRepository.existsById(itemId)).isTrue();

            Item createdItem = itemRepository.getById(itemId).get();

            assertThat(createdItem.getName()).isEqualTo("Twix");
            assertThat(createdItem.getDescription()).isEqualTo("A delicious chocolate bar with caramel and a crunchy bite.");
            assertThat(createdItem.getPrice()).isEqualTo(1.0);
            assertThat(createdItem.getAmount()).isEqualTo(153);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                """
                        {
                            "description": "A delicious chocolate bar with caramel and a crunchy bite.",
                            "price": 1.0,
                            "amount": 153
                        },
                        {
                            "name": "Twix",
                            "price": 1.0,
                            "amount": 153
                        },
                        {
                            "name": "Twix",
                            "description": "A delicious chocolate bar with caramel and a crunchy bite.",
                            "amount": 153
                        },
                        {
                            "name": "Twix",
                            "description": "A delicious chocolate bar with caramel and a crunchy bite.",
                            "price": 1.0
                        }
                        """
        })
        void givenMissingField_whenCreateItem_thenBadRequest(String createItemRequestWithMissingField) throws Exception {
            // Given + When + Then
            mockMvc.perform(post("/items")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createItemRequestWithMissingField))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "  ", "\n", "\t"})
        void givenIllegalName_whenCreatingItem_thenBadRequest(String illegalName) throws Exception {
            // Given
            String createItemRequestWithIllegalName = """
                    {
                        "name": "%s",
                        "description": "A delicious chocolate bar with caramel and a crunchy bite.",
                        "price": 1.0,
                        "amount": 153
                    }
                    """.formatted(illegalName);

            // When + Then
            mockMvc.perform(post("/items")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createItemRequestWithIllegalName))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "  ", "\n", "\t"})
        void givenIllegalDescription_whenCreatingItem_thenBadRequest(String Description) throws Exception {
            // Given
            String createItemRequestWithIllegalDescription = """
                    {
                        "name": "Twix",
                        "description": "%s",
                        "price": 1.0,
                        "amount": 153
                    }
                    """.formatted(Description);

            // When + Then
            mockMvc.perform(post("/items")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createItemRequestWithIllegalDescription))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void givenNegativePrice_whenCreatingItem_thenBadRequest() throws Exception {
            // Given
            String createItemRequestWithNegativePrice = """
                    {
                        "name": "Twix",
                        "description": "A delicious chocolate bar with caramel and a crunchy bite.",
                        "price": -1.0,
                        "amount": 153
                    }
                    """;

            // When + Then
            mockMvc.perform(post("/items")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createItemRequestWithNegativePrice))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void givenNegativeAmount_whenCreatingItem_thenBadRequest() throws Exception {
            // Given
            String createItemRequestWithNegativeAmount = """
                    {
                        "name": "Twix",
                        "description": "A delicious chocolate bar with caramel and a crunchy bite.",
                        "price": 1.0,
                        "amount": -1
                    }
                    """;

            // When + Then
            mockMvc.perform(post("/items")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createItemRequestWithNegativeAmount))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void givenItemsInRepository_whenGetItems_thenListOfItemsWith200StatusCode() throws Exception {
        // Given
        Item item1 = new Item(
                "Twix",
                "A delicious chocolate bar with caramel and a crunchy bite.",
                1.0,
                15
        );

        Item item2 = new Item(
                "Bounty",
                "A chocolate bar with coconut flavor.",
                0.8,
                13
        );

        itemRepository.putItem(item1);
        itemRepository.putItem(item2);

        // When
        ResultActions result = mockMvc.perform(get("/items")
                .accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON)
        );

        List<ItemResponseDTO> actualItems = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(actualItems)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(List.of(itemMapper.toResponseDTO(item1), itemMapper.toResponseDTO(item2)));
    }
}