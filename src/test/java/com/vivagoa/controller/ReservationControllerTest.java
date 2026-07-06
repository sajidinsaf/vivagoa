package com.vivagoa.controller;

import com.vivagoa.config.SecurityConfig;
import com.vivagoa.entity.Reservation;
import com.vivagoa.service.AdminUserService;
import com.vivagoa.service.ContactService;
import com.vivagoa.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private ContactService contactService;

    @MockitoBean
    private AdminUserService adminUserService;

    @Test
    void getReservations_shouldReturn200AndReservationView() throws Exception {
        when(contactService.getContactInfo()).thenReturn(Optional.empty());

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attributeExists("reservation"));
    }

    @Test
    void postReservations_shouldSaveAndRedirectWithSuccess() throws Exception {
        Reservation savedReservation = new Reservation();
        savedReservation.setId(1L);
        savedReservation.setGuestName("John Doe");
        savedReservation.setStatus(Reservation.Status.CONFIRMED);

        when(reservationService.save(any(Reservation.class))).thenReturn(savedReservation);

        mockMvc.perform(post("/reservations")
                        .with(csrf())
                        .param("guestName", "John Doe")
                        .param("email", "john@example.com")
                        .param("phone", "9876543210")
                        .param("partySize", "4")
                        .param("reservationDate", "2026-07-15")
                        .param("reservationTime", "19:00")
                        .param("specialRequests", "Window seat please"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservations"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(reservationService, times(1)).save(any(Reservation.class));
    }

    @Test
    void postReservations_shouldRejectWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/reservations")
                        .param("guestName", "John Doe")
                        .param("partySize", "4")
                        .param("reservationDate", "2026-07-15")
                        .param("reservationTime", "19:00"))
                .andExpect(status().isForbidden());
    }
}
