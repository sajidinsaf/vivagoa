package com.vivagoa.service;

import com.vivagoa.entity.MenuItem;
import com.vivagoa.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    private final MenuItemRepository menuItemRepository;

    public MenuService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem save(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public Optional<MenuItem> findById(Long id) {
        return menuItemRepository.findById(id);
    }

    public Optional<MenuItem> findByNameAndCategory(String name, String category) {
        return menuItemRepository.findByNameAndCategory(name, category);
    }

    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    public List<MenuItem> findByCategory(String category) {
        return menuItemRepository.findByCategoryOrderByDisplayOrder(category);
    }

    public List<MenuItem> findAvailable() {
        return menuItemRepository.findByAvailableTrue();
    }

    public Map<String, List<MenuItem>> getMenuGroupedByCategory() {
        List<MenuItem> allItems = menuItemRepository.findByAvailableTrueOrderByCategoryAscDisplayOrderAsc();
        return allItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory, LinkedHashMap::new, Collectors.toList()));
    }

    public Map<String, List<MenuItem>> getAllMenuGroupedByCategory() {
        List<MenuItem> allItems = menuItemRepository.findAll();
        return allItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory, LinkedHashMap::new, Collectors.toList()));
    }

    public void delete(Long id) {
        menuItemRepository.deleteById(id);
    }
}
