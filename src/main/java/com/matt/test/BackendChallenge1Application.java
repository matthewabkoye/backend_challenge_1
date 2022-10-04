package com.matt.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.matt.test")
public class BackendChallenge1Application {

	public static void main(String[] args) {
		SpringApplication.run(BackendChallenge1Application.class, args);
	}

}
