package com.vivagoa.service;

import com.vivagoa.entity.Reservation;
import com.vivagoa.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setGuestName("John Doe");
        reservation.setEmail("john@example.com");
        reservation.setPhone("1234567890");
        reservation.setPartySize(4);
        reservation.setReservationDate(LocalDate.of(2026, 7, 10));
        reservation.setReservationTime(LocalTime.of(19, 0));
        reservation.setStatus(Reservation.Status.CONFIRMED);
    }

    @Test
    void save_shouldSaveAndReturnReservation() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation saved = reservationService.save(reservation);

        assertThat(saved).isNotNull();
        assertThat(saved.getGuestName()).isEqualTo("John Doe");
        assertThat(saved.getPartySize()).isEqualTo(4);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void findAll_shouldReturnAllReservations() {
        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setGuestName("Jane Smith");
        reservation2.setPartySize(2);
        reservation2.setReservationDate(LocalDate.of(2026, 7, 11));
        reservation2.setReservationTime(LocalTime.of(20, 0));
        reservation2.setStatus(Reservation.Status.CONFIRMED);

        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation, reservation2));

        List<Reservation> result = reservationService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getGuestName()).isEqualTo("John Doe");
        assertThat(result.get(1).getGuestName()).isEqualTo("Jane Smith");
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoReservations() {
        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());

        List<Reservation> result = reservationService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void findByDateRange_shouldReturnReservationsBetweenDates() {
        LocalDate start = LocalDate.of(2026, 7, 1);
        LocalDate end = LocalDate.of(2026, 7, 15);

        when(reservationRepository.findByReservationDateBetween(start, end))
                .thenReturn(Collections.singletonList(reservation));

        List<Reservation> result = reservationService.findByDateRange(start, end);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getReservationDate()).isEqualTo(LocalDate.of(2026, 7, 10));
        verify(reservationRepository).findByReservationDateBetween(start, end);
    }

    @Test
    void findByDate_shouldReturnReservationsForDate() {
        LocalDate date = LocalDate.of(2026, 7, 10);

        when(reservationRepository.findByReservationDateOrderByReservationTimeAsc(date))
                .thenReturn(Collections.singletonList(reservation));

        List<Reservation> result = reservationService.findByDate(date);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getGuestName()).isEqualTo("John Doe");
        verify(reservationRepository).findByReservationDateOrderByReservationTimeAsc(date);
    }

    @Test
    void findByStatus_shouldReturnReservationsWithStatus() {
        when(reservationRepository.findByStatus(Reservation.Status.CONFIRMED))
                .thenReturn(Collections.singletonList(reservation));

        List<Reservation> result = reservationService.findByStatus(Reservation.Status.CONFIRMED);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(Reservation.Status.CONFIRMED);
        verify(reservationRepository).findByStatus(Reservation.Status.CONFIRMED);
    }

    @Test
    void findByStatus_shouldReturnEmptyListForNoMatch() {
        when(reservationRepository.findByStatus(Reservation.Status.CANCELLED))
                .thenReturn(Collections.emptyList());

        List<Reservation> result = reservationService.findByStatus(Reservation.Status.CANCELLED);

        assertThat(result).isEmpty();
    }

    @Test
    void updateStatus_shouldUpdateAndReturnReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation updated = reservationService.updateStatus(1L, Reservation.Status.COMPLETED);

        assertThat(updated).isNotNull();
        assertThat(updated.getStatus()).isEqualTo(Reservation.Status.COMPLETED);
        verify(reservationRepository).findById(1L);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void updateStatus_shouldThrowExceptionWhenNotFound() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.updateStatus(99L, Reservation.Status.CANCELLED))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Reservation not found");
    }

    @Test
    void updateBilling_shouldSetBillingAmountAndReturn() {
        BigDecimal amount = new BigDecimal("150.00");
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation updated = reservationService.updateBilling(1L, amount);

        assertThat(updated).isNotNull();
        assertThat(updated.getBillingAmount()).isEqualTo(amount);
        verify(reservationRepository).findById(1L);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void updateBilling_shouldThrowExceptionWhenNotFound() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.updateBilling(99L, new BigDecimal("100.00")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Reservation not found");
    }
}
