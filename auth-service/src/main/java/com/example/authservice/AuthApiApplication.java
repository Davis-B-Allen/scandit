package com.example.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class AuthApiApplication {

    @RequestMapping("/")
    public String home() {
        return "valid auth";
    }

	public static void main(String[] args) {
		SpringApplication.run(AuthApiApplication.class, args);
	}

}
