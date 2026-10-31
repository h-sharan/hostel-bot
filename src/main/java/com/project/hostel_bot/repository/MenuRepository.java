package com.project.hostel_bot.repository;

import com.project.hostel_bot.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findByMenuDate(LocalDate menuDate);
}