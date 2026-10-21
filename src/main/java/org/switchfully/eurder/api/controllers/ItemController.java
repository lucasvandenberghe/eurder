package org.switchfully.eurder.api.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.switchfully.eurder.api.dtos.item.ItemCreationRequestDTO;
import org.switchfully.eurder.api.dtos.item.ItemResponseDTO;
import org.switchfully.eurder.api.mappers.ItemMapper;
import org.switchfully.eurder.domain.Item;
import org.switchfully.eurder.services.ItemService;

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
    public ItemResponseDTO createItem(@Valid @RequestBody ItemCreationRequestDTO itemCreationRequestDTO) {
        Item itemToCreate = itemMapper.toItem(itemCreationRequestDTO);

        return itemMapper.toResponseDTO(itemService.createItem(itemToCreate));
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemResponseDTO> getItems() {
        return itemMapper.toResponseDTO(itemService.getItems());
    }
}
