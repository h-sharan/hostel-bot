package com.project.hostel_bot.controller;

import com.project.hostel_bot.model.Student;
import com.project.hostel_bot.repository.StudentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Student register(@RequestBody Student student) {
        return repository.save(student);
    }

    @GetMapping
    public List<Student> getAll() {
        return repository.findAll();
    }
}