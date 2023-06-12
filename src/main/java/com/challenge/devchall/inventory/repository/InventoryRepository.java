package com.challenge.devchall.inventory.repository;

import com.challenge.devchall.inventory.entity.Inventory;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
        public Optional<Inventory> findByMemberAndItem(Member member, Item item);
}
