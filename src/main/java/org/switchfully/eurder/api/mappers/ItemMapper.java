package org.switchfully.eurder.api.mappers;

import org.springframework.stereotype.Component;
import org.switchfully.eurder.api.dtos.item.ItemCreationRequestDTO;
import org.switchfully.eurder.api.dtos.item.ItemResponseDTO;
import org.switchfully.eurder.domain.Item;

import java.util.List;

@Component
public class ItemMapper {

    public Item toItem(ItemCreationRequestDTO itemCreationRequestDTO) {
        return new Item(
                itemCreationRequestDTO.name(),
                itemCreationRequestDTO.description(),
                itemCreationRequestDTO.price(),
                itemCreationRequestDTO.amount()
        );
    }

    public ItemResponseDTO toResponseDTO(Item item) {
        return new ItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getAmount()
        );
    }

    public List<ItemResponseDTO> toResponseDTO(List<Item> items) {
        return items.stream().map(this::toResponseDTO).toList();
    }
}
