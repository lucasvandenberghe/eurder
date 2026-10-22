package org.switchfully.eurder.api.dtos.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ItemCreationRequestDTO(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotNull
        @PositiveOrZero
        double price,
        @NotNull
        @PositiveOrZero
        int amount
) {
}
