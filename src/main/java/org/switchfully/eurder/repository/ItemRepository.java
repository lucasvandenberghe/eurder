package org.switchfully.eurder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.switchfully.eurder.domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
