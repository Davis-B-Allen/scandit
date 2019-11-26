package com.example.profileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.*;

/**
 * The main application class.
 * Used to run as a Spring application.
 * */
@SpringBootApplication
@EnableEurekaClient
@RestController
public class ProfileApiApplication {

	/**
	 * Runs the profile-service application.
	 * @param args an array of strings that can be passed as arguments.
	 * */
	public static void main(String[] args) {
		SpringApplication.run(ProfileApiApplication.class, args);
	}

}
