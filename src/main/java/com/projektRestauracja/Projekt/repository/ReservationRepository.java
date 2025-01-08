package com.projektRestauracja.Projekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projektRestauracja.Projekt.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
