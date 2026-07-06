package com.vivagoa.service;

import com.vivagoa.entity.MenuItem;
import com.vivagoa.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class MenuServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuService menuService;

    private MenuItem starterItem;
    private MenuItem mainItem;

    @BeforeEach
    void setUp() {
        starterItem = new MenuItem();
        starterItem.setId(1L);
        starterItem.setName("Prawn Balchao");
        starterItem.setDescription("Spicy Goan prawn dish");
        starterItem.setPrice(new BigDecimal("350.00"));
        starterItem.setCategory("Starters");
        starterItem.setVegetarian(false);
        starterItem.setAvailable(true);
        starterItem.setDisplayOrder(1);

        mainItem = new MenuItem();
        mainItem.setId(2L);
        mainItem.setName("Chicken Xacuti");
        mainItem.setDescription("Goan chicken curry with roasted spices");
        mainItem.setPrice(new BigDecimal("450.00"));
        mainItem.setCategory("Main Course");
        mainItem.setVegetarian(false);
        mainItem.setAvailable(true);
        mainItem.setDisplayOrder(1);
    }

    @Test
    void save_shouldSaveAndReturnMenuItem() {
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(starterItem);

        MenuItem saved = menuService.save(starterItem);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Prawn Balchao");
        assertThat(saved.getPrice()).isEqualTo(new BigDecimal("350.00"));
        verify(menuItemRepository, times(1)).save(starterItem);
    }

    @Test
    void findAll_shouldReturnAllMenuItems() {
        when(menuItemRepository.findAll()).thenReturn(Arrays.asList(starterItem, mainItem));

        List<MenuItem> result = menuService.findAll();

        assertThat(result).hasSize(2);
        verify(menuItemRepository).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoItems() {
        when(menuItemRepository.findAll()).thenReturn(Collections.emptyList());

        List<MenuItem> result = menuService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void findByCategory_shouldReturnItemsForCategory() {
        when(menuItemRepository.findByCategoryOrderByDisplayOrder("Starters"))
                .thenReturn(Collections.singletonList(starterItem));

        List<MenuItem> result = menuService.findByCategory("Starters");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("Starters");
        assertThat(result.get(0).getName()).isEqualTo("Prawn Balchao");
        verify(menuItemRepository).findByCategoryOrderByDisplayOrder("Starters");
    }

    @Test
    void findByCategory_shouldReturnEmptyListForNoMatch() {
        when(menuItemRepository.findByCategoryOrderByDisplayOrder("Desserts"))
                .thenReturn(Collections.emptyList());

        List<MenuItem> result = menuService.findByCategory("Desserts");

        assertThat(result).isEmpty();
    }

    @Test
    void getMenuGroupedByCategory_shouldReturnLinkedHashMapGroupedByCategory() {
        when(menuItemRepository.findByAvailableTrueOrderByCategoryAscDisplayOrderAsc())
                .thenReturn(Arrays.asList(mainItem, starterItem));

        Map<String, List<MenuItem>> result = menuService.getMenuGroupedByCategory();

        assertThat(result).isInstanceOf(LinkedHashMap.class);
        assertThat(result).containsKeys("Main Course", "Starters");
        assertThat(result.get("Starters")).hasSize(1);
        assertThat(result.get("Main Course")).hasSize(1);
        assertThat(result.get("Starters").get(0).getName()).isEqualTo("Prawn Balchao");
        assertThat(result.get("Main Course").get(0).getName()).isEqualTo("Chicken Xacuti");
    }

    @Test
    void getMenuGroupedByCategory_shouldReturnEmptyMapWhenNoItems() {
        when(menuItemRepository.findByAvailableTrueOrderByCategoryAscDisplayOrderAsc())
                .thenReturn(Collections.emptyList());

        Map<String, List<MenuItem>> result = menuService.getMenuGroupedByCategory();

        assertThat(result).isEmpty();
    }

    @Test
    void getMenuGroupedByCategory_shouldGroupMultipleItemsInSameCategory() {
        MenuItem starterItem2 = new MenuItem();
        starterItem2.setId(3L);
        starterItem2.setName("Vegetable Samosa");
        starterItem2.setPrice(new BigDecimal("150.00"));
        starterItem2.setCategory("Starters");
        starterItem2.setAvailable(true);
        starterItem2.setDisplayOrder(2);

        when(menuItemRepository.findByAvailableTrueOrderByCategoryAscDisplayOrderAsc())
                .thenReturn(Arrays.asList(starterItem, starterItem2));

        Map<String, List<MenuItem>> result = menuService.getMenuGroupedByCategory();

        assertThat(result).hasSize(1);
        assertThat(result.get("Starters")).hasSize(2);
    }
}
