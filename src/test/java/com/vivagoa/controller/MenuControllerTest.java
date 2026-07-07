package com.vivagoa.controller;

import com.vivagoa.config.SecurityConfig;
import com.vivagoa.entity.MenuItem;
import com.vivagoa.service.AdminUserService;
import com.vivagoa.service.ContactService;
import com.vivagoa.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuService menuService;

    @MockitoBean
    private ContactService contactService;

    @MockitoBean
    private AdminUserService adminUserService;

    @Test
    void getMenu_shouldReturn200AndMenuView() throws Exception {
        MenuItem item = new MenuItem();
        item.setId(1L);
        item.setName("Fish Curry");
        item.setPrice(new BigDecimal("300.00"));
        item.setCategory("Main Course");
        item.setAvailable(true);

        Map<String, List<MenuItem>> menuMap = new LinkedHashMap<>();
        menuMap.put("Main Course", Collections.singletonList(item));

        when(menuService.getMenuGroupedByCategory()).thenReturn(menuMap);
        when(contactService.getContactInfo()).thenReturn(Optional.empty());

        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(view().name("menu"))
                .andExpect(model().attributeExists("menuItems"));
    }

    @Test
    void getMenu_shouldContainMenuItemsGroupedByCategory() throws Exception {
        MenuItem starter = new MenuItem();
        starter.setId(1L);
        starter.setName("Samosa");
        starter.setPrice(new BigDecimal("100.00"));
        starter.setCategory("Starters");

        MenuItem main = new MenuItem();
        main.setId(2L);
        main.setName("Vindaloo");
        main.setPrice(new BigDecimal("400.00"));
        main.setCategory("Main Course");

        Map<String, List<MenuItem>> menuMap = new LinkedHashMap<>();
        menuMap.put("Starters", Collections.singletonList(starter));
        menuMap.put("Main Course", Collections.singletonList(main));

        when(menuService.getMenuGroupedByCategory()).thenReturn(menuMap);
        when(contactService.getContactInfo()).thenReturn(Optional.empty());

        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(view().name("menu"))
                .andExpect(model().attributeExists("menuItems"));
    }
}
