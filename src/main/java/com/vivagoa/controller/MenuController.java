package com.vivagoa.controller;

import com.vivagoa.entity.MenuItem;
import com.vivagoa.service.ContactService;
import com.vivagoa.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class MenuController {

    private final MenuService menuService;
    private final ContactService contactService;

    public MenuController(MenuService menuService, ContactService contactService) {
        this.menuService = menuService;
        this.contactService = contactService;
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        Map<String, List<MenuItem>> menuByCategory = menuService.getMenuGroupedByCategory();
        model.addAttribute("menuByCategory", menuByCategory);
        contactService.getContactInfo().ifPresent(info -> model.addAttribute("contactInfo", info));
        return "menu";
    }
}
