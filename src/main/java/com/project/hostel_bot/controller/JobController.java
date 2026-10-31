package com.project.hostel_bot.controller;

import com.project.hostel_bot.model.JobStatus;
import com.project.hostel_bot.repository.MessageJobRepository;
import com.project.hostel_bot.service.JobCreatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobCreatorService creator;
    private final MessageJobRepository jobRepository;

    public JobController(JobCreatorService creator, MessageJobRepository jobRepository) {
        this.creator = creator;
        this.jobRepository = jobRepository;
    }

    @PostMapping("/create-today")
    public String createToday() {
        int created = creator.createJobsFor(LocalDate.now());
        return "Created " + created + " jobs";
    }

    @GetMapping("/summary")
    public Map<String, Long> summary() {
        return Map.of(
                "PENDING", jobRepository.countByStatus(JobStatus.PENDING),
                "SENT", jobRepository.countByStatus(JobStatus.SENT),
                "FAILED", jobRepository.countByStatus(JobStatus.FAILED));
    }
}