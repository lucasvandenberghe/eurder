package org.switchfully.eurder.services.exceptions;

public class CustomerNotFoundException extends CustomerServiceException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
