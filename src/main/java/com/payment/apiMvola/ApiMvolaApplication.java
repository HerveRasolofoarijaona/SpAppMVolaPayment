package com.payment.apiMvola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.payment.repository")
@EntityScan(basePackages = "com.payment.model")
public class ApiMvolaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiMvolaApplication.class, args);
	}

}
