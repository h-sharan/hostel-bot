package com.project.hostel_bot.repository;

import com.project.hostel_bot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByActiveTrue();
}
