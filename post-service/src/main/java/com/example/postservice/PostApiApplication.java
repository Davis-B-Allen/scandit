package com.example.postservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("com.example.postservice.client")
@RestController
public class PostApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostApiApplication.class, args);
	}

}
