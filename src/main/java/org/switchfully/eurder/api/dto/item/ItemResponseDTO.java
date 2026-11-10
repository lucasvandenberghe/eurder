package org.switchfully.eurder.api.dto.item;

public record ItemResponseDTO(
        long id,
        String name,
        String description,
        double price,
        int amount
) {
}
