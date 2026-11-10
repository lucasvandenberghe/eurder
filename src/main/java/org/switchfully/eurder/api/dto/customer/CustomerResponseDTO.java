package org.switchfully.eurder.api.dto.customer;

public record CustomerResponseDTO(long id,
                                  String firstName,
                                  String lastName,
                                  String emailAddress,
                                  String address,
                                  String phoneNumber) {
}
