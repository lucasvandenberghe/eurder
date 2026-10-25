package org.switchfully.eurder.api.dtos.order;

import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(UUID orderId,
                               UUID customerId,
                               List<ItemGroupResponseDTO> itemGroups,
                               double totalPrice) {
}
