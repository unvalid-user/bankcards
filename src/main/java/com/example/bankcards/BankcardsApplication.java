package com.example.bankcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankcardsApplication {
	public static void main(String[] args) {
		SpringApplication.run(BankcardsApplication.class, args);

		System.out.println("Swagger: http://localhost:8080/swagger-ui/index.html");
	}

}
