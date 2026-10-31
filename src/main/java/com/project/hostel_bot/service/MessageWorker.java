package com.project.hostel_bot.service;

import com.project.hostel_bot.model.JobStatus;
import com.project.hostel_bot.model.Menu;
import com.project.hostel_bot.model.MessageJob;
import com.project.hostel_bot.model.Student;
import com.project.hostel_bot.repository.MenuRepository;
import com.project.hostel_bot.repository.MessageJobRepository;
import com.project.hostel_bot.repository.StudentRepository;
import com.project.hostel_bot.sender.MessageSender;
import com.project.hostel_bot.sender.PermanentSendException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageWorker {

    private static final int MAX_ATTEMPTS = 5;
    private static final long BASE_DELAY_SECONDS = 5;
    private static final long PAUSE_BETWEEN_SENDS_MS = 100; // about 10 messages per second

    private final MessageJobRepository jobRepository;
    private final StudentRepository studentRepository;
    private final MenuRepository menuRepository;
    private final MessageSender sender;

    public MessageWorker(MessageJobRepository jobRepository,
                         StudentRepository studentRepository,
                         MenuRepository menuRepository,
                         MessageSender sender) {
        this.jobRepository = jobRepository;
        this.studentRepository = studentRepository;
        this.menuRepository = menuRepository;
        this.sender = sender;
    }

    // Runs 3 seconds after the previous run finishes
    @Scheduled(fixedDelay = 3000)
    public void processBatch() {
        List<MessageJob> jobs = jobRepository
                .findTop20ByStatusAndNextAttemptAtLessThanEqualOrderByNextAttemptAtAsc(
                        JobStatus.PENDING, LocalDateTime.now());

        for (MessageJob job : jobs) {
            process(job);
            pause();
        }
    }

    private void process(MessageJob job) {
        job.setAttempts(job.getAttempts() + 1);
        try {
            Student student = studentRepository.findById(job.getStudentId())
                    .orElseThrow(() -> new PermanentSendException("Student not found"));
            Menu menu = menuRepository.findByMenuDate(job.getJobDate())
                    .orElseThrow(() -> new PermanentSendException("No menu for " + job.getJobDate()));

            sender.send(student.getPhone(), buildMessage(menu));

            job.setStatus(JobStatus.SENT);
            job.setLastError(null);

        } catch (PermanentSendException e) {
            job.setStatus(JobStatus.FAILED);
            job.setLastError(e.getMessage());
            System.out.println("[FAILED permanently] job " + job.getId() + ": " + e.getMessage());

        } catch (RuntimeException e) {
            job.setLastError(e.getMessage());
            if (job.getAttempts() >= MAX_ATTEMPTS) {
                job.setStatus(JobStatus.FAILED);
                System.out.println("[GAVE UP] job " + job.getId() + " after " + job.getAttempts() + " attempts");
            } else {
                long delay = BASE_DELAY_SECONDS * (1L << (job.getAttempts() - 1)); // 5, 10, 20, 40 seconds
                job.setNextAttemptAt(LocalDateTime.now().plusSeconds(delay));
                System.out.println("[RETRY] job " + job.getId() + " attempt " + job.getAttempts()
                        + ", next try in " + delay + "s");
            }
        }
        jobRepository.save(job);
    }

    private String buildMessage(Menu menu) {
        return "Good morning! Menu for " + menu.getMenuDate() + "\n"
                + "Breakfast (7:30-9:00): " + menu.getBreakfast() + "\n"
                + "Lunch (12:30-2:00): " + menu.getLunch() + "\n"
                + "Snacks (4:30-5:30): " + menu.getSnacks() + "\n"
                + "Dinner (7:30-9:00): " + menu.getDinner();
    }

    private void pause() {
        try {
            Thread.sleep(PAUSE_BETWEEN_SENDS_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}