package com.vivagoa.repository;

import com.vivagoa.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByReservationDateBetween(LocalDate startDate, LocalDate endDate);

    List<Reservation> findByStatus(Reservation.Status status);

    List<Reservation> findByReservationDateOrderByReservationTimeAsc(LocalDate date);
}
