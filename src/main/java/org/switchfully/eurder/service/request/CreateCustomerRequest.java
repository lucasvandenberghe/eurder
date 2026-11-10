package org.switchfully.eurder.service.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(@NotBlank
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
