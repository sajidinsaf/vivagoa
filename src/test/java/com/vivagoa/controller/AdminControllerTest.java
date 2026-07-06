package com.vivagoa.controller;

import com.vivagoa.config.SecurityConfig;
import com.vivagoa.entity.*;
import com.vivagoa.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private GalleryService galleryService;

    @MockitoBean
    private MenuService menuService;

    @MockitoBean
    private ContactService contactService;

    @MockitoBean
    private AdminUserService adminUserService;

    // --- Authenticated tests ---

    @Test
    @WithMockUser(roles = "ADMIN")
    void getDashboard_withAdmin_shouldReturn200() throws Exception {
        when(reservationService.findByDate(any(LocalDate.class))).thenReturn(Collections.emptyList());
        when(contactService.getUnreadMessages()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"))
                .andExpect(model().attributeExists("todayReservations"))
                .andExpect(model().attributeExists("totalToday"))
                .andExpect(model().attributeExists("unreadMessages"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getReservations_withAdmin_shouldReturn200() throws Exception {
        when(reservationService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/reservations"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reservations"))
                .andExpect(model().attributeExists("reservations"))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getGallery_withAdmin_shouldReturn200() throws Exception {
        when(galleryService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/gallery"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/gallery"))
                .andExpect(model().attributeExists("images"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("newImage"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getContact_withAdmin_shouldReturn200() throws Exception {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(1L);
        contactInfo.setEmail("info@vivagoa.com");
        when(contactService.getContactInfo()).thenReturn(Optional.of(contactInfo));

        mockMvc.perform(get("/admin/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/contact"))
                .andExpect(model().attributeExists("contactInfo"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getMessages_withAdmin_shouldReturn200() throws Exception {
        when(contactService.getAllMessages()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/messages"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/messages"))
                .andExpect(model().attributeExists("messages"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void postUpdateStatus_withAdmin_shouldUpdateAndRedirect() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatus(Reservation.Status.COMPLETED);

        when(reservationService.updateStatus(eq(1L), eq(Reservation.Status.COMPLETED)))
                .thenReturn(reservation);

        mockMvc.perform(post("/admin/reservations/1/status")
                        .with(csrf())
                        .param("status", "COMPLETED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/reservations"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(reservationService).updateStatus(1L, Reservation.Status.COMPLETED);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void postUpdateBilling_withAdmin_shouldUpdateAndRedirect() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setBillingAmount(new BigDecimal("250.00"));

        when(reservationService.updateBilling(eq(1L), any(BigDecimal.class)))
                .thenReturn(reservation);

        mockMvc.perform(post("/admin/reservations/1/billing")
                        .with(csrf())
                        .param("amount", "250.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/reservations"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(reservationService).updateBilling(eq(1L), eq(new BigDecimal("250.00")));
    }

    // --- Unauthenticated tests ---

    @Test
    void getDashboard_withoutAuth_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/admin/login"));
    }

    @Test
    void getReservations_withoutAuth_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/admin/reservations"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/admin/login"));
    }

    @Test
    void getGallery_withoutAuth_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/admin/gallery"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/admin/login"));
    }

    @Test
    void getMessages_withoutAuth_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/admin/messages"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/admin/login"));
    }
}
