package com.example.postservice;

import com.example.postservice.client.AuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("com.example.postservice.client")
@RestController
public class PostApiApplication {

    @Autowired
    AuthClient authClient;

    @RequestMapping("/")
    public String home() {
        return "some most excellent posts";
    }

    @GetMapping("/myposts")
    public String myPosts() {
        String auth = authClient.getAuth();
        if (auth.equals("valid auth")) {
            return "my posts";
        }
        return "NOT AUTHED";
    }

    @PostMapping("/post")
    public String createPost(@RequestHeader("Authorization") String bearerToken) {
        System.out.println(bearerToken);
        ResponseEntity<String> authResponse = null;
        try {
            authResponse = authClient.createPostAuth(bearerToken);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
        String authString = authResponse.getBody();
        System.out.println(authString);
        return "Post Created!";
    }

	public static void main(String[] args) {
		SpringApplication.run(PostApiApplication.class, args);
	}

}
