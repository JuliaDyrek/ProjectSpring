package com.projektRestauracja.Projekt.repository;

import com.projektRestauracja.Projekt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
