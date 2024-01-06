package com.foriserver.fori;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ForiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForiApplication.class, args);
	}

}
