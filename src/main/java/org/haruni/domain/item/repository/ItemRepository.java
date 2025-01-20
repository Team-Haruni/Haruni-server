package org.haruni.domain.item.repository;

import org.haruni.domain.item.entity.Item;
import org.haruni.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    void deleteAllByUser(User user);
}
