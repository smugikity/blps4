package com.football;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration.class
})
public class Lab4SpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(Lab4SpringApplication.class, args);

	}

}
