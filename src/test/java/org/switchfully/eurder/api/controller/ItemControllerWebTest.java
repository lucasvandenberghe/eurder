package org.switchfully.eurder.api.controller;

import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
public class ItemControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

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
                        .content("""
                                {
                                    "name": "test",
                                    "description": "testdescription",
                                    "price": 5,
                                    "amount": 21
                                }
                                """))
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

    @Test
}
