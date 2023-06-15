package com.challenge.devchall.inventory.service;

import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.inventory.entity.Inventory;
import com.challenge.devchall.inventory.repository.InventoryRepository;
import com.challenge.devchall.item.entity.Item;
import com.challenge.devchall.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public RsData<Inventory> create(Member member, Item item, boolean equipped){
        if(isPurchased(member, item)){
            return RsData.of("F-1", "이미 구매한 아이템입니다.");
        }
        Inventory inventory = inventoryRepository.save(Inventory.builder()
                .member(member)
                .item(item)
                .equipped(equipped)
                .build());
        return RsData.of("S-1", "구매에 성공하였습니다.", inventory);
    }

    public boolean isPurchased(Member member, Item item){ //true: 이미 구매함, false: 처음 구매
        return inventoryRepository.findByMemberAndItem(member, item).isPresent();
    }

    public void changeFontEquip(long itemId, Member member) {

        if(member!=null){
            member.getEquippedFont().unequip();

            for(Inventory inventory: member.getInventoryList()){

                if(inventory.getItem().getId()==itemId){

                    inventory.equip();

                    return;
                }
            }
        }
    }

    public void changeCharacterEquip(long itemId, Member member) {
        if(member!=null){

            member.getEquippedCharacter().unequip();

            for(Inventory inventory: member.getInventoryList()){

                if(inventory.getItem().getId()==itemId){

                    inventory.equip();

                    return;
                }
            }
        }
    }
}
