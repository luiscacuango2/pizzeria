package com.luigi.pizza;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
public class LuigiPizzeriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LuigiPizzeriaApplication.class, args);
	}

}
