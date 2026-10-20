package org.switchfully.eurder.domain;

import org.switchfully.eurder.domain.exceptions.CustomerException;

import java.util.Objects;
import java.util.UUID;

public class Customer {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final String address;
    private final String phoneNumber;

    public Customer(String firstName, String lastName, String emailAddress, String address, String phoneNumber) {
        if (firstName == null || firstName.isBlank())
            throw new CustomerException("Please provide a non-blank firstName.");
        if (lastName == null || lastName.isBlank())
            throw new CustomerException("Please provide a non-blank lastName.");
        if (emailAddress == null || emailAddress.isBlank())
            throw new CustomerException("Please provide a non-blank emailAddress.");
        if (address == null || address.isBlank())
            throw new CustomerException("Please provide a non-blank address.");
        if (phoneNumber == null || phoneNumber.isBlank())
            throw new CustomerException("Please provide a non-blank phoneNumber.");

        id = UUID.randomUUID();
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
