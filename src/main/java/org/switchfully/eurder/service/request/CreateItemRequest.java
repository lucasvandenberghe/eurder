package org.switchfully.eurder.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateItemRequest(@NotBlank
                                String name,
                                @NotBlank
                                String description,
                                @NotNull
                                @PositiveOrZero
                                double price,
                                @NotNull
                                @PositiveOrZero
                                int amount) {
}
