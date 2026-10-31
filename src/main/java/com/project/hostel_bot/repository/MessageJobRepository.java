package com.project.hostel_bot.repository;

import com.project.hostel_bot.model.JobStatus;
import com.project.hostel_bot.model.MessageJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MessageJobRepository extends JpaRepository<MessageJob, Long> {

    List<MessageJob> findTop20ByStatusAndNextAttemptAtLessThanEqualOrderByNextAttemptAtAsc(
            JobStatus status, LocalDateTime now);

    boolean existsByStudentIdAndJobDate(Long studentId, LocalDate jobDate);

    long countByStatus(JobStatus status);
}