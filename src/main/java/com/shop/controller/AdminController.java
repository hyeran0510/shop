package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.entity.Item;
import com.shop.repository.ItemRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ItemRepository itemRepository;

    @GetMapping("/items/new")
    public String showItemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "admin/itemForm";  // 상품 등록 페이지
    }

    @PostMapping("/items/new")
    public String addItem(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/itemForm";
        }

        // Item 엔티티 생성 후 저장
        Item item = new Item();
        item.updateItem(itemFormDto);  // ItemFormDto 데이터를 Item 엔티티에 반영
        itemRepository.save(item);     // 상품 저장

        return "redirect:/admin/items";
    }
}
