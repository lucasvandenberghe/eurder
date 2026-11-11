package org.switchfully.eurder.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.switchfully.eurder.api.dto.item.ItemResponseDTO;
import org.switchfully.eurder.api.mapper.ItemMapper;
import org.switchfully.eurder.service.ItemService;
import org.switchfully.eurder.service.request.CreateItemRequest;

import java.util.List;

@RestController
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDTO createItem(@Valid @RequestBody CreateItemRequest createItemRequest) {
        return itemMapper.toResponseDTO(itemService.createItem(createItemRequest));
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemResponseDTO> getAllItems() {
        return itemMapper.toResponseDTO(itemService.getAllItems());
    }
}
