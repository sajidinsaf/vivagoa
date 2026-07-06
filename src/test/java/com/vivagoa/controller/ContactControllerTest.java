package com.vivagoa.controller;

import com.vivagoa.config.SecurityConfig;
import com.vivagoa.entity.ContactInfo;
import com.vivagoa.entity.ContactMessage;
import com.vivagoa.service.AdminUserService;
import com.vivagoa.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    @MockitoBean
    private AdminUserService adminUserService;

    @Test
    void getContact_shouldReturn200AndContactView() throws Exception {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(1L);
        contactInfo.setEmail("info@vivagoa.com");
        when(contactService.getContactInfo()).thenReturn(Optional.of(contactInfo));

        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("contactMessage"))
                .andExpect(model().attributeExists("contactInfo"));
    }

    @Test
    void getContact_shouldWorkWithoutContactInfo() throws Exception {
        when(contactService.getContactInfo()).thenReturn(Optional.empty());

        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("contactMessage"))
                .andExpect(model().attributeDoesNotExist("contactInfo"));
    }

    @Test
    void postContact_shouldSaveMessageAndRedirectWithSuccess() throws Exception {
        ContactMessage savedMessage = new ContactMessage();
        savedMessage.setId(1L);
        savedMessage.setName("Test User");
        savedMessage.setEmail("test@example.com");
        savedMessage.setSubject("Inquiry");
        savedMessage.setMessage("Hello");

        when(contactService.saveMessage(any(ContactMessage.class))).thenReturn(savedMessage);

        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .param("name", "Test User")
                        .param("email", "test@example.com")
                        .param("phone", "9876543210")
                        .param("subject", "Inquiry")
                        .param("message", "Hello"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(contactService, times(1)).saveMessage(any(ContactMessage.class));
    }

    @Test
    void postContact_shouldRejectWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/contact")
                        .param("name", "Test User")
                        .param("email", "test@example.com")
                        .param("message", "Hello"))
                .andExpect(status().isForbidden());
    }
}
