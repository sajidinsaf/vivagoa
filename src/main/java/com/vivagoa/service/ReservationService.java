package com.vivagoa.service;

import com.vivagoa.entity.Reservation;
import com.vivagoa.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findByReservationDateBetween(startDate, endDate);
    }

    public List<Reservation> findByStatus(Reservation.Status status) {
        return reservationRepository.findByStatus(status);
    }

    public List<Reservation> findByDate(LocalDate date) {
        return reservationRepository.findByReservationDateOrderByReservationTimeAsc(date);
    }

    public Reservation updateStatus(Long id, Reservation.Status status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }

    public Reservation updateBilling(Long id, BigDecimal amount) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        reservation.setBillingAmount(amount);
        return reservationRepository.save(reservation);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
