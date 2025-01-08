package com.projektRestauracja.Projekt.repository;

import com.projektRestauracja.Projekt.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
