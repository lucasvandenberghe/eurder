package org.switchfully.eurder.api.dtos.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerCreationRequestDTO(@NotBlank
                                         String firstName,
                                         @NotBlank
                                         String lastName,
                                         @NotBlank
                                         @Email
                                         String emailAddress,
                                         @NotBlank
                                         String address,
                                         @NotBlank
                                         String phoneNumber) {
}