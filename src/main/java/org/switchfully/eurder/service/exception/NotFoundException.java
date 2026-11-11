package org.switchfully.eurder.service.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }

  public static NotFoundException customerNotFound(long customerId) {
      return new NotFoundException("Customer with id " + customerId + " not found.");
  }
}
