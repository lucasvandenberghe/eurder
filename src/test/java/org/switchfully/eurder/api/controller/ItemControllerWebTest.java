package org.switchfully.eurder.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.switchfully.eurder.api.dto.item.ItemResponseDTO;
import org.switchfully.eurder.api.mapper.ItemMapper;
import org.switchfully.eurder.domain.Item;
import org.switchfully.eurder.service.ItemService;
import org.switchfully.eurder.service.request.CreateItemRequest;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
public class ItemControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private ItemMapper itemMapper;

    @Test
    void givenValidRequest_whenCreateItem_thenItemCreatedWith201() throws Exception {
        Item mockItem = new Item(
                "test",
                "testdescription",
                5,
                21
        );
        ReflectionTestUtils.setField(mockItem, "id", 1L);

        when(itemService.createItem(any(CreateItemRequest.class)))
                .thenReturn(mockItem);

        ItemResponseDTO itemResponseDTO = new ItemResponseDTO(
                mockItem.getId(),
                mockItem.getName(),
                mockItem.getDescription(),
                mockItem.getPrice(),
                mockItem.getAmount()
        );

        when(itemMapper.toResponseDTO(mockItem))
                .thenReturn(itemResponseDTO);

        mockMvc.perform(post("/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockItem)))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(1L),
                        jsonPath("$.name").value("test"),
                        jsonPath("$.description").value("testdescription"),
                        jsonPath("$.price").value(5),
                        jsonPath("$.amount").value(21)
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            """
                        {
                            "name": "",
                            "description": "testdescription",
                            "price": 5,
                            "amount": 21
                        }
                    """,
            """
                        {
                            "name": null,
                            "description": "testdescription",
                            "price": 5,
                            "amount": 21
                        }
                    """,
            """
                        {
                            "description": "testdescription",
                            "price": 5,
                            "amount": 21
                        }
                    """,
            """
                        {
                            "name": "test",
                            "description": "",
                            "price": 5,
                            "amount": 21
                        }
                    """,
            """
                        {
                            "name": "test",
                            "description": null,
                            "price": 5,
                            "amount": 21
                        }
                    """,
            """
                        {
                            "name": "test",
                            "price": 5,
                            "amount": 21
                        }
                    """,
            """
                        {
                            "name": "test",
                            "description": "testdescription",
                            "price": -1,
                            "amount": 21
                        }
                    """,
            """
                        {
                            "name": "test",
                            "description": "testdescription",
                            "amount": 21
                        }
                    """,
            """
                        {
                            "name": "test",
                            "description": "testdescription",
                            "price": 5,
                            "amount": -1
                        }
                    """,
            """
                        {
                            "name": "test",
                            "description": "testdescription",
                            "price": 5
                        }
                    """
    })
    void givenInvalidRequest_whenCreateItem_thenBadRequest(String invalidRequest) throws Exception {
        mockMvc.perform(post("/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenSavedItems_whenGetAllItems_thenListOfItemsReturnedWith200() throws Exception {
        Item item1 = new Item(
                "test1",
                "testdescription1",
                5,
                20
        );
        ReflectionTestUtils.setField(item1, "id", 1L);

        Item item2 = new Item(
                "test2",
                "testdescription2",
                6,
                21
        );
        ReflectionTestUtils.setField(item2, "id", 2L);

        List<Item> items = List.of(item1, item2);

        when(itemService.getAllItems())
                .thenReturn(items);

        when(itemMapper.toResponseDTO(items))
                .thenReturn(List.of(new ItemResponseDTO(
                                item1.getId(),
                                item1.getName(),
                                item1.getDescription(),
                                item1.getPrice(),
                                item1.getAmount()
                        ), new ItemResponseDTO(
                                item2.getId(),
                                item2.getName(),
                                item2.getDescription(),
                                item2.getPrice(),
                                item2.getAmount()
                        )
                ));

        mockMvc.perform(get("/items")
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.length()").value(2),
                        jsonPath("$[?(@.id == 1)].name").value("test1"),
                        jsonPath("$[?(@.id == 2)].name").value("test2"),
                        jsonPath("$[?(@.id == 1)].description").value("testdescription1"),
                        jsonPath("$[?(@.id == 2)].description").value("testdescription2"),
                        jsonPath("$[?(@.id == 1)].price").value(5.0),
                        jsonPath("$[?(@.id == 2)].price").value(6.0),
                        jsonPath("$[?(@.id == 1)].amount").value(20),
                        jsonPath("$[?(@.id == 2)].amount").value(21)
                );
    }
}
