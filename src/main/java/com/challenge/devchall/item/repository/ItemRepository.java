package com.challenge.devchall.item.repository;

import com.challenge.devchall.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    public Optional<Item> findByName(String name);
}

