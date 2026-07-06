package com.vivagoa.controller;

import com.vivagoa.config.SecurityConfig;
import com.vivagoa.entity.GalleryImage;
import com.vivagoa.service.AdminUserService;
import com.vivagoa.service.ContactService;
import com.vivagoa.service.GalleryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GalleryController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class GalleryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GalleryService galleryService;

    @MockitoBean
    private ContactService contactService;

    @MockitoBean
    private AdminUserService adminUserService;

    @Test
    void getGallery_shouldReturn200AndGalleryView() throws Exception {
        GalleryImage foodImage = new GalleryImage();
        foodImage.setId(1L);
        foodImage.setFileName("food.jpg");
        foodImage.setCategory(GalleryImage.Category.FOOD);
        foodImage.setActive(true);

        GalleryImage ambianceImage = new GalleryImage();
        ambianceImage.setId(2L);
        ambianceImage.setFileName("ambiance.jpg");
        ambianceImage.setCategory(GalleryImage.Category.AMBIANCE);
        ambianceImage.setActive(true);

        when(galleryService.findActiveImages()).thenReturn(Arrays.asList(foodImage, ambianceImage));
        when(galleryService.findActiveByCategoryOrdered(GalleryImage.Category.FOOD))
                .thenReturn(Collections.singletonList(foodImage));
        when(galleryService.findActiveByCategoryOrdered(GalleryImage.Category.AMBIANCE))
                .thenReturn(Collections.singletonList(ambianceImage));
        when(galleryService.findActiveByCategoryOrdered(GalleryImage.Category.EVENTS))
                .thenReturn(Collections.emptyList());
        when(contactService.getContactInfo()).thenReturn(Optional.empty());

        mockMvc.perform(get("/gallery"))
                .andExpect(status().isOk())
                .andExpect(view().name("gallery"))
                .andExpect(model().attributeExists("allImages"))
                .andExpect(model().attributeExists("foodImages"))
                .andExpect(model().attributeExists("ambianceImages"))
                .andExpect(model().attributeExists("eventImages"));
    }

    @Test
    void getGallery_shouldContainCategorizedImages() throws Exception {
        when(galleryService.findActiveImages()).thenReturn(Collections.emptyList());
        when(galleryService.findActiveByCategoryOrdered(GalleryImage.Category.FOOD))
                .thenReturn(Collections.emptyList());
        when(galleryService.findActiveByCategoryOrdered(GalleryImage.Category.AMBIANCE))
                .thenReturn(Collections.emptyList());
        when(galleryService.findActiveByCategoryOrdered(GalleryImage.Category.EVENTS))
                .thenReturn(Collections.emptyList());
        when(contactService.getContactInfo()).thenReturn(Optional.empty());

        mockMvc.perform(get("/gallery"))
                .andExpect(status().isOk())
                .andExpect(view().name("gallery"))
                .andExpect(model().attributeExists("allImages"))
                .andExpect(model().attributeExists("foodImages"))
                .andExpect(model().attributeExists("ambianceImages"))
                .andExpect(model().attributeExists("eventImages"));
    }
}
