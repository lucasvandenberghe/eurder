package org.switchfully.eurder.api.dtos.customer;

import java.util.UUID;

public record CustomerResponseDTO(UUID id,
                                  String firstName,
                                  String lastName,
                                  String emailAddress,
                                  String address,
                                  String phoneNumber) {
}
