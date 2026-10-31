package com.project.hostel_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HostelBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(HostelBotApplication.class, args);
	}

}
