package com.projektRestauracja.Projekt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projektRestauracja.Projekt.model.CustomerOrder;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findAllByReservationId(Long reservationId);
}
