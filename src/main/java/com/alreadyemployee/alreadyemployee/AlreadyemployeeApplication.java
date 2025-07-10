package com.alreadyemployee.alreadyemployee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlreadyemployeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlreadyemployeeApplication.class, args);
	}

}
