package com.project.hostel_bot.sender;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class FakeSender implements MessageSender {

    @Override
    public void send(String phone, String text) {
        if (ThreadLocalRandom.current().nextInt(100) < 30) {
            throw new RetryableSendException("429 Too Many Requests (simulated)");
        }
        System.out.println("[FAKE SEND] to " + phone);
    }
}