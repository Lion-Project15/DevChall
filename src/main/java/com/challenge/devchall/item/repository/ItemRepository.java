package com.challenge.devchall.item.repository;

import com.challenge.devchall.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}

