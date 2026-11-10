package org.switchfully.eurder.service;

import org.springframework.stereotype.Service;
import org.switchfully.eurder.domain.Item;
import org.switchfully.eurder.repository.ItemRepository;
import org.switchfully.eurder.service.exception.ItemServiceException;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(Item item) {
        if (itemRepository.existsById(item.getId()))
            throw new ItemServiceException("Item with that id already exists.");

        return itemRepository.putItem(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.getAllItems();
    }
}
