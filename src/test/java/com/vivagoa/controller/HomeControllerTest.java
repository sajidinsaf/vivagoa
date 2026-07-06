package com.vivagoa.controller;

import com.vivagoa.config.SecurityConfig;
import com.vivagoa.entity.ContactInfo;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GalleryService galleryService;

    @MockitoBean
    private ContactService contactService;

    @MockitoBean
    private AdminUserService adminUserService;

    @Test
    void getHome_shouldReturn200AndHomeView() throws Exception {
        GalleryImage image = new GalleryImage();
        image.setId(1L);
        image.setFileName("test.jpg");
        image.setTitle("Test Image");
        image.setCategory(GalleryImage.Category.FOOD);
        image.setActive(true);

        List<GalleryImage> images = Arrays.asList(image);
        when(galleryService.findActiveImages()).thenReturn(images);

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(1L);
        contactInfo.setEmail("info@vivagoa.com");
        when(contactService.getContactInfo()).thenReturn(Optional.of(contactInfo));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("galleryImages"))
                .andExpect(model().attributeExists("contactInfo"));
    }

    @Test
    void getHome_shouldContainGalleryImagesInModel() throws Exception {
        GalleryImage image1 = new GalleryImage();
        image1.setId(1L);
        image1.setFileName("food1.jpg");
        image1.setCategory(GalleryImage.Category.FOOD);
        image1.setActive(true);

        GalleryImage image2 = new GalleryImage();
        image2.setId(2L);
        image2.setFileName("ambiance1.jpg");
        image2.setCategory(GalleryImage.Category.AMBIANCE);
        image2.setActive(true);

        when(galleryService.findActiveImages()).thenReturn(Arrays.asList(image1, image2));
        when(contactService.getContactInfo()).thenReturn(Optional.empty());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("galleryImages"))
                .andExpect(model().attributeDoesNotExist("contactInfo"));
    }
}
