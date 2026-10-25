package org.switchfully.eurder.api.dtos.order;

import java.time.LocalDate;
import java.util.UUID;

public record ItemGroupResponseDTO(UUID itemId,
                                   String itemName,
                                   double itemPrice,
                                   int amount,
                                   LocalDate shippingDate) {
}
