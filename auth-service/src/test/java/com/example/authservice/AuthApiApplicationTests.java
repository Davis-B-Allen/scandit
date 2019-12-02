package com.example.authservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("qa,global")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
