package org.switchfully.eurder.service;

import org.springframework.stereotype.Service;
import org.switchfully.eurder.domain.Item;
import org.switchfully.eurder.repository.ItemRepository;
import org.switchfully.eurder.service.request.CreateItemRequest;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(CreateItemRequest createItemRequest) {
        Item item = new Item(
                createItemRequest.name(),
                createItemRequest.description(),
                createItemRequest.price(),
                createItemRequest.amount()
        );

        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}
