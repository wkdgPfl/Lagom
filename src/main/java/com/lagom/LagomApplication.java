package com.lagom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LagomApplication {

	public static void main(String[] args) {
		SpringApplication.run(LagomApplication.class, args);
	}

}
