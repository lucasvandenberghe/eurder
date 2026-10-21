package org.switchfully.eurder.repositories;

import org.springframework.stereotype.Repository;
import org.switchfully.eurder.domain.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ItemRepository {

    private final Map<UUID, Item> idToItem;

    public ItemRepository() {
        idToItem = new HashMap<>();
    }


    public boolean existsById(UUID id) {
        return idToItem.containsKey(id);
    }

    public Item putItem(Item item) {
        idToItem.put(item.getId(), item);

        return item;
    }

    public void clear() {
        idToItem.clear();
    }

    public Optional<Item> getById(UUID itemId) {
        return Optional.ofNullable(idToItem.get(itemId));
    }
}
