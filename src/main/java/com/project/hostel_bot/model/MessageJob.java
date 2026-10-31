package com.project.hostel_bot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "job_date"}))
public class MessageJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private LocalDate jobDate;

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.PENDING;

    private int attempts = 0;
    private LocalDateTime nextAttemptAt = LocalDateTime.now();
    private String lastError;

    public MessageJob() {
    }

    public MessageJob(Long studentId, LocalDate jobDate) {
        this.studentId = studentId;
        this.jobDate = jobDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public LocalDate getJobDate() { return jobDate; }
    public void setJobDate(LocalDate jobDate) { this.jobDate = jobDate; }

    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }

    public int getAttempts() { return attempts; }
    public void setAttempts(int attempts) { this.attempts = attempts; }

    public LocalDateTime getNextAttemptAt() { return nextAttemptAt; }
    public void setNextAttemptAt(LocalDateTime nextAttemptAt) { this.nextAttemptAt = nextAttemptAt; }

    public String getLastError() { return lastError; }
    public void setLastError(String lastError) { this.lastError = lastError; }
}