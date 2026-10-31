package com.project.hostel_bot.controller;

import com.project.hostel_bot.model.Student;
import com.project.hostel_bot.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

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
    @PostMapping("/seed/{count}")
    public String seed(@PathVariable int count) {
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Student s = new Student();
            s.setName("Test " + i);
            s.setPhone("9100000" + String.format("%05d", i));
            list.add(s);
        }
        repository.saveAll(list);
        return "Seeded " + count + " students";
    }

}