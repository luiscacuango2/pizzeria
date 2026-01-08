package com.luigi.pizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class LuigiPizzeriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LuigiPizzeriaApplication.class, args);
	}

}
