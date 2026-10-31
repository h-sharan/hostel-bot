package com.project.hostel_bot.service;

import com.project.hostel_bot.model.MessageJob;
import com.project.hostel_bot.model.Student;
import com.project.hostel_bot.repository.MessageJobRepository;
import com.project.hostel_bot.repository.StudentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobCreatorService {

    private final StudentRepository studentRepository;
    private final MessageJobRepository jobRepository;

    public JobCreatorService(StudentRepository studentRepository, MessageJobRepository jobRepository) {
        this.studentRepository = studentRepository;
        this.jobRepository = jobRepository;
    }

    // Runs every day at 6:30 AM India time
    @Scheduled(cron = "0 30 6 * * *", zone = "Asia/Kolkata")
    public void createTodaysJobs() {
        createJobsFor(LocalDate.now());
    }

    public int createJobsFor(LocalDate date) {
        List<Student> students = studentRepository.findByActiveTrue();
        int created = 0;
        for (Student s : students) {
            if (!jobRepository.existsByStudentIdAndJobDate(s.getId(), date)) {
                jobRepository.save(new MessageJob(s.getId(), date));
                created++;
            }
        }
        System.out.println("Created " + created + " jobs for " + date);
        return created;
    }
}