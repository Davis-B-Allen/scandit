package com.example.profileservice;

import com.example.profileservice.client.AuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("com.example.profileservice.client")
@RestController
public class ProfileApiApplication {

	@Autowired
	AuthClient authClient;

	public static void main(String[] args) {
		SpringApplication.run(ProfileApiApplication.class, args);
	}

}
