package com.challenge.devchall.item.service;

import com.challenge.devchall.base.rsData.RsData;
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

        public RsData<Item> create(String name, String type, String pattern, String subPattern, long price){
                RsData<Item> rsItem = RsData.of("S-1", "아이템 생성 성공");
                if(isExisted(name)){
                    return RsData.of("F-1","이미 존재하는 아이템입니다.");
                }
               rsItem.setData(
                   itemRepository.save(Item.builder()
                       .name(name)
                       .type(type)
                       .pattern(pattern)
                       .subPattern(subPattern)
                       .price(price)
                       .build())
               );
                return rsItem;
        }

    private boolean isExisted(String name) {
            return getByName(name).isPresent();
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
