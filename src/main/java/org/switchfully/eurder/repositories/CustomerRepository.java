package org.switchfully.eurder.repositories;

import org.springframework.stereotype.Repository;
import org.switchfully.eurder.domain.Customer;

import java.util.*;

@Repository
public class CustomerRepository {

    private final Map<UUID, Customer> idToCustomer;

    public CustomerRepository() {
        idToCustomer = new HashMap<>();
    }

    public boolean existsById(UUID id) {
        return idToCustomer.containsKey(id);
    }

    public Customer putCustomer(Customer customer) {
        idToCustomer.put(customer.getId(), customer);

        return customer;
    }

    public void clear() {
        idToCustomer.clear();
    }

    public Optional<Customer> getById(UUID id) {
        return Optional.ofNullable(idToCustomer.get(id));
    }

    public List<Customer> getAllCustomers() {
        return idToCustomer.values().stream().toList();
    }
}
