package com.challenge.devchall.inventory.controller;

import com.challenge.devchall.base.rq.Rq;
import com.challenge.devchall.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    private final Rq rq;

    @GetMapping("/change/{item}")
    public String changeEquip(@PathVariable("item") long itemId){
        inventoryService.changeFontEquip(itemId, rq.getMember());
        return "redirect:/usr/member/store";
    }

    @GetMapping("/me/change/{item}")
    public String changeEquipMypage(@PathVariable("item") long itemId){
        inventoryService.changeFontEquip(itemId, rq.getMember());
        return "redirect:/usr/member/me";
    }
}
