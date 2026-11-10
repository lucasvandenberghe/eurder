package org.switchfully.eurder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.switchfully.eurder.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
