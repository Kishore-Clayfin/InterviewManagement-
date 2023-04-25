package com.cf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class InterviewManagementApplication {

	public static void main(String[] args) {
		//hrtest1@gmail.com-->HRtest1
		//interTest1
		SpringApplication.run(InterviewManagementApplication.class, args);
	}

}