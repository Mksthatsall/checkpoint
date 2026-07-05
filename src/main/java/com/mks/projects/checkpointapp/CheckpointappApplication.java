package com.mks.projects.checkpointapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CheckpointappApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheckpointappApplication.class, args);
	}

}
