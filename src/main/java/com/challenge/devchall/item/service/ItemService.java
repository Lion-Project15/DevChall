package com.challenge.devchall.item.service;

import com.challenge.devchall.item.entity.Item;
import com.challenge.devchall.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService   {
        private final ItemRepository itemRepository;

        public Item create(String name, String type, String pattern, String subPattern,long price){
               return itemRepository.save(Item.builder()
                       .name(name)
                       .type(type)
                       .pattern(pattern)
                       .subPattern(subPattern)
                       .price(price)
                       .build());
        }

        public Optional<Item> getByName(String name){
            return itemRepository.findByName(name);
        }

    public List<Item> getByType(String type) {
            return itemRepository.findByType(type);
    }

    public Optional<Item> getById(long itemId) {
            return itemRepository.findById(itemId);
    }
}
