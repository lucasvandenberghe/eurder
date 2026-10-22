package org.switchfully.eurder.api.dtos.item;

import java.util.UUID;

public record ItemResponseDTO(
        UUID id,
        String name,
        String description,
        double price,
        int amount
) {
}
