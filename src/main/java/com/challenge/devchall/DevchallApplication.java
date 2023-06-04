package com.challenge.devchall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevchallApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevchallApplication.class, args);
	}

}
